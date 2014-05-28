/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.SearchPerson.SearchParameters;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.ContextSelectionBean;
import net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.StudentContextSelectionBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.log.FirstYearShiftsCapacityToggleLog;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarRootEntry;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYearCE;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.externalSupervision.consult.ShowStudentTimeTable;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.RAMApplication.RAMSchedulesApp;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.CollectionPager;

/**
 * @author Luis Cruz & Sara Ribeiro
 */

@StrutsFunctionality(app = RAMSchedulesApp.class, path = "manage", titleKey = "link.schedules.chooseContext")
@Mapping(path = "/chooseExecutionPeriod", module = "resourceAllocationManager")
@Forwards({
        @Forward(name = "toggleFirstYearShiftsCapacity", path = "/resourceAllocationManager/toggleFirstYearShiftsCapacity_bd.jsp"),
        @Forward(name = "showForm", path = "/resourceAllocationManager/chooseExecutionPeriod_bd.jsp"),
        @Forward(name = "showTimeTable", path = "/resourceAllocationManager/showTimetable.jsp") })
public class ExecutionPeriodDA extends FenixContextDispatchAction {

    static private final Integer FIRST_CURRICULAR_YEAR = Integer.valueOf(1);

    private static final Comparator<ExecutionDegree> executionDegreeComparator = new Comparator<ExecutionDegree>() {
        @Override
        public int compare(ExecutionDegree executionDegree1, ExecutionDegree executionDegree2) {
            final Degree degree1 = executionDegree1.getDegreeCurricularPlan().getDegree();
            final Degree degree2 = executionDegree2.getDegreeCurricularPlan().getDegree();

            int degreeTypeComparison = degree1.getDegreeType().compareTo(degree2.getDegreeType());
            return (degreeTypeComparison != 0) ? degreeTypeComparison : degree1.getNome().compareTo(degree2.getNome());
        }
    };

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return prepare(mapping, form, request, response);
    }

    @EntryPoint
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ContextSelectionBean contextSelectionBean =
                (ContextSelectionBean) request.getAttribute(PresentationConstants.CONTEXT_SELECTION_BEAN);

        final StudentContextSelectionBean studentContextSelectionBean =
                new StudentContextSelectionBean(contextSelectionBean.getAcademicInterval());
        request.setAttribute("studentContextSelectionBean", studentContextSelectionBean);

        final List<ExecutionDegree> executionDegrees =
                new ArrayList<ExecutionDegree>(ExecutionDegree.filterByAcademicInterval(contextSelectionBean
                        .getAcademicInterval()));
        Collections.sort(executionDegrees, executionDegreeComparator);
        request.setAttribute("executionDegrees", executionDegrees);
        ExecutionSemester executionSemester =
                (ExecutionSemester) ExecutionInterval.getExecutionInterval(contextSelectionBean.getAcademicInterval());
        request.setAttribute("executionSemester", executionSemester);

        AcademicCalendarEntry academicCalendarEntry = contextSelectionBean.getAcademicInterval().getAcademicCalendarEntry();
        while (!(academicCalendarEntry instanceof AcademicCalendarRootEntry)) {
            if (academicCalendarEntry instanceof AcademicYearCE) {
                ExecutionYear year = ExecutionYear.getExecutionYear((AcademicYearCE) academicCalendarEntry);
                request.setAttribute("executionYear", year);
                break;
            } else {
                academicCalendarEntry = academicCalendarEntry.getParentEntry();
            }
        }

        if (!executionSemester.isCurrent()) {
            request.setAttribute("noEditionAllowed", true);
        }

        return mapping.findForward("showForm");
    }

    public ActionForward chooseStudent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final StudentContextSelectionBean studentContextSelectionBean = getRenderedObject();

        final String number = studentContextSelectionBean.getNumber();
        if (number != null && !number.isEmpty()) {
            final AcademicInterval academicInterval = studentContextSelectionBean.getAcademicInterval();
            final ExecutionInterval executionInterval = ExecutionInterval.getExecutionInterval(academicInterval);

            final SearchParameters searchParameters = new SearchParameters();
            if (StringUtils.isNumeric(number)) {
                searchParameters.setStudentNumber(Integer.valueOf(number));
            } else {
                searchParameters.setUsername(number);
            }
            final CollectionPager<Person> people =
                    new SearchPerson().run(searchParameters, new SearchPerson.SearchPersonPredicate(searchParameters));
            final Collection<Registration> registrations = new ArrayList<Registration>();
            for (final Person person : people.getCollection()) {
                if (person.hasStudent()) {
                    for (final Registration registration : person.getStudent().getRegistrationsSet()) {
                        if (registration.hasAnyActiveState((ExecutionSemester) executionInterval)) {
                            registrations.add(registration);
                        }
                    }
                }
            }

            if (studentContextSelectionBean.getToEdit()) {
                request.setAttribute("toEditScheduleRegistrations", registrations);
            } else {
                request.setAttribute("registrations", registrations);
                request.setAttribute("timeTableExecutionSemester", executionInterval);
            }

        }

        return prepare(mapping, form, request, response);
    }

    public ActionForward chooseStudentById(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Registration registration = getDomainObject(request, "registrationId");
        final ExecutionSemester executionSemester = getDomainObject(request, "executionSemesterId");

        request.setAttribute("registration", registration);
        return new ShowStudentTimeTable().forwardToShowTimeTable(registration, mapping, request, executionSemester);
    }

    public ActionForward toggleFirstYearShiftsCapacity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("executionYear", getDomainObject(request, "executionYearId"));
        return mapping.findForward("toggleFirstYearShiftsCapacity");
    }

    public ActionForward blockFirstYearShiftsCapacity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionYear executionYear = getDomainObject(request, "executionYearId");
        Map<ExecutionDegree, Integer> modified = setFirstYearShiftsCapacity(true, executionYear);

        request.setAttribute("affectedDegrees", getAffectedDegreesInfo(modified));
        request.setAttribute("executionYear", executionYear);
        return mapping.findForward("toggleFirstYearShiftsCapacity");
    }

    public ActionForward unblockFirstYearShiftsCapacity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionYear executionYear = getDomainObject(request, "executionYearId");
        Map<ExecutionDegree, Integer> modified = setFirstYearShiftsCapacity(false, executionYear);
        request.setAttribute("affectedDegrees", getAffectedDegreesInfo(modified));
        request.setAttribute("executionYear", executionYear);
        return mapping.findForward("toggleFirstYearShiftsCapacity");
    }

    private List<String> getAffectedDegreesInfo(Map<ExecutionDegree, Integer> modified) {
        List<String> modifiedDescription = new ArrayList<String>();

        for (Map.Entry<ExecutionDegree, Integer> entry : modified.entrySet()) {
            modifiedDescription.add(new String(entry.getKey().getPresentationName() + " :  " + entry.getValue()));
        }
        Collections.sort(modifiedDescription);
        return modifiedDescription;
    }

    @Atomic
    private Map<ExecutionDegree, Integer> setFirstYearShiftsCapacity(Boolean toBlock, ExecutionYear executionYear) {

        final ExecutionSemester executionSemester = executionYear.getFirstExecutionPeriod();

        final Map<Shift, Set<ExecutionDegree>> shiftsDegrees = new HashMap<Shift, Set<ExecutionDegree>>();
        final Set<Shift> shifts = new HashSet<Shift>();
        final Map<ExecutionDegree, Integer> modified = new HashMap<ExecutionDegree, Integer>();

        for (final Degree degree : Degree.readAllByDegreeType(DegreeType.BOLONHA_DEGREE,
                DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE)) {
            for (final DegreeCurricularPlan degreeCurricularPlan : degree.getActiveDegreeCurricularPlans()) {
                final ExecutionDegree executionDegree =
                        degreeCurricularPlan.getExecutionDegreeByAcademicInterval(executionSemester.getExecutionYear()
                                .getAcademicInterval());

                if (executionDegree != null) {
                    for (final SchoolClass schoolClass : executionDegree.getSchoolClassesSet()) {
                        if (schoolClass.getAnoCurricular().equals(FIRST_CURRICULAR_YEAR)
                                && schoolClass.getExecutionPeriod() == executionSemester) {
                            for (final Shift shift : schoolClass.getAssociatedShiftsSet()) {
                                Set<ExecutionDegree> executionDegrees = shiftsDegrees.get(shift);
                                if (executionDegrees == null) {
                                    executionDegrees = new HashSet<ExecutionDegree>();
                                }
                                executionDegrees.add(executionDegree);
                                shiftsDegrees.put(shift, executionDegrees);
                                shifts.add(shift);
                            }
                        }
                    }
                }
            }
        }

        for (final Shift shift : shifts) {
            int capacity = shift.getLotacao().intValue();

            if (toBlock && capacity > 0) {
                shift.setLotacao(capacity * -1);
            } else if (!toBlock && capacity < 0) {
                shift.setLotacao(capacity * -1);
            } else {
                continue;
            }

            for (ExecutionDegree executionDegree : shiftsDegrees.get(shift)) {
                if (modified.containsKey(executionDegree)) {
                    modified.put(executionDegree, modified.get(executionDegree) + 1);
                } else {
                    modified.put(executionDegree, 1);
                }
            }
        }

        if (modified.size() > 0) {
            new FirstYearShiftsCapacityToggleLog(executionYear.getFirstExecutionPeriod(), Authenticate.getUser().getPerson()
                    .getUser());
        }

        return modified;
    }

}
