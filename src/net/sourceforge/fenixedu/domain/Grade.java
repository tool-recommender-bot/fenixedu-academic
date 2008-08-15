package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Grade implements Serializable, Comparable<Grade> {

    private static Grade emptyGrade = new EmptyGrade();

    private static Map<String, Grade> gradeMap = new HashMap<String, Grade>();

    private String value;

    private GradeScale gradeScale;

    protected Grade() {

    }

    protected Grade(String value, GradeScale gradeScale) {
	if (EmptyGrade.qualifiesAsEmpty(value)) {
	    throw new DomainException("error.grade.invalid.argument");
	}

	if (!gradeScale.belongsTo(value)) {
	    throw new DomainException("error.grade.invalid.grade");
	}

	setValue(value);
	setGradeScale(gradeScale);
    }

    public int compareTo(final Grade otherGrade) {
	final boolean isApproved = isApproved();
	final boolean isApprovedOther = otherGrade.isApproved();
	if (isApproved && isApprovedOther) {
	    if (getValue().equals(GradeScale.AP)) {
		return 1;
	    } else if (otherGrade.getValue().equals(GradeScale.AP)) {
		return -1;
	    } else if (getGradeScale().equals(otherGrade.getGradeScale())) {
		return getValue().compareTo(otherGrade.getValue());
	    } else {
		throw new DomainException("Grade.unsupported.comparassion.of.grades.of.different.scales");
	    }
	} else if (isApproved || otherGrade.getValue().equals(GradeScale.NA) || otherGrade.getValue().equals(GradeScale.RE)) {
	    return 1;
	} else if (isApprovedOther || getValue().equals(GradeScale.NA) || getValue().equals(GradeScale.RE)) {
	    return -1;
	} else {
	    return getValue().compareTo(otherGrade.getValue());
	}
    }

    public BigDecimal getNumericValue() {
	return value == null ? null : new BigDecimal(getValue());
    }

    public String getValue() {
	return value;
    }

    private void setValue(String value) {
	this.value = value.trim().toUpperCase();
    }

    public GradeScale getGradeScale() {
	return gradeScale;
    }

    private void setGradeScale(GradeScale gradeScale) {
	this.gradeScale = gradeScale;
    }

    public static Grade createGrade(String value, GradeScale gradeScale) {
	if (EmptyGrade.qualifiesAsEmpty(value)) {
	    return createEmptyGrade();
	}

	Grade grade = gradeMap.get(exportAsString(gradeScale, value));
	if (grade == null) {
	    grade = new Grade(value, gradeScale);
	    gradeMap.put(grade.exportAsString(), grade);
	}
	return grade;
    }

    public static Grade createEmptyGrade() {
	return emptyGrade;
    }

    public static Grade importFromString(String string) {
	if (EmptyGrade.qualifiesAsEmpty(string)) {
	    return emptyGrade;
	}

	String[] tokens = string.split(":");
	return createGrade(tokens[1], GradeScale.valueOf(tokens[0]));
    }

    @Override
    public String toString() {
	return exportAsString();
    }

    public String exportAsString() {
	return exportAsString(getGradeScale(), getValue());
    }

    private static String exportAsString(GradeScale gradeScale, String value) {
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append(gradeScale);
	stringBuilder.append(":");
	stringBuilder.append(value.trim().toUpperCase());

	return stringBuilder.toString();
    }

    public boolean isEmpty() {
	return false;
    }

    public boolean isNumeric() {
	/**
	 * 
	 * This was the original implementation, but it is very slow...
	 * especially when generating reports that have to call this
	 * method for practically every enrollment.
	
	try {
	    Double.parseDouble(getValue());
	    return true;
	} catch (NumberFormatException e) {
	    return false;
	}

	 * 
	 * This alternative implementation is roughly 30-40x faster!
	 * So I suggest you keep this ugly code where it is.
	 * Using StringUtils may appear to be nicer, but beware that
	 * it will produce different results!
	 * 
	 */
	if (value.isEmpty()) {
	    return false;
	}
	boolean foundSeperator = false;
	for (int i = 0; i < value.length(); i++) {
	    final char c = value.charAt(i);
	    if (c == '.' || c == ',') {
		if (foundSeperator) {
		    return false;
		} else {
		    foundSeperator = true;
		}
	    } else if (!Character.isDigit(c)) {
		return false;
	    }
	}
	return true;
    }

    public boolean isApproved() {
	return getGradeScale().isApproved(this);
    }

    public boolean isNotApproved() {
	return getGradeScale().isNotApproved(this);
    }

    public boolean isNotEvaluated() {
	return getGradeScale().isNotEvaluated(this);
    }

    public String getEctsScale() {
	return GradeDistribution.ECTS_SCALE_20.getDistribution(this).getScale();
    }

    static public Grade average(@SuppressWarnings("unused") final Collection<Grade> grades) {
	return null;
    }

    public EnrollmentState getEnrolmentState() {
	if (isNotEvaluated()) {
	    return EnrollmentState.NOT_EVALUATED;
	} else if (isNotApproved()) {
	    return EnrollmentState.NOT_APROVED;
	} else if (isApproved()) {
	    return EnrollmentState.APROVED;
	} else {
	    return EnrollmentState.NOT_APROVED;
	}
    }

}
