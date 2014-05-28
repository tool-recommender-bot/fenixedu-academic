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
/*
 *
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class SwitchPublishedExamsFlag {

    @Atomic
    public static void run(AcademicInterval academicInterval) {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        final List<ExecutionDegree> executionDegrees = ExecutionDegree.filterByAcademicInterval(academicInterval);
        ExecutionSemester executionSemester = (ExecutionSemester) ExecutionInterval.getExecutionInterval(academicInterval);

        final Boolean isToRemove =
                new Boolean(executionDegrees.iterator().next().getPublishedExamMapsSet().contains(executionSemester));

        for (final ExecutionDegree executionDegree : executionDegrees) {
            if (isToRemove) {
                executionDegree.getPublishedExamMapsSet().remove(executionSemester);
            } else {
                executionDegree.getPublishedExamMapsSet().add(executionSemester);
            }
        }
    }
}
