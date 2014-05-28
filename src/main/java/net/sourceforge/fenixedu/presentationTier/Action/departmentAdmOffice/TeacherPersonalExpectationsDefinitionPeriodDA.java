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
package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.commons.ExecutionYearBean;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.TeacherExpectationDefinitionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.DepartmentAdmOfficeApp.DefineExpectationPeriods;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "departmentAdmOffice", path = "/teacherPersonalExpectationsDefinitionPeriod",
        functionality = DefineExpectationPeriods.class)
@Forwards({
        @Forward(
                name = "showDefinitionPeriod",
                path = "/departmentAdmOffice/teacher/teacherPersonalExpectationsManagement/showTeacherPersonalExpectationsDefinitionPeriod.jsp"),
        @Forward(
                name = "editDefinitionPeriod",
                path = "/departmentAdmOffice/teacher/teacherPersonalExpectationsManagement/editTeacherPersonalExpectationsDefinitionPeriod.jsp") })
public class TeacherPersonalExpectationsDefinitionPeriodDA extends FenixDispatchAction {

    public ActionForward showPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionYear year = ExecutionYear.readCurrentExecutionYear();
        readAndSetPeriod(request, year);
        request.setAttribute("bean", new ExecutionYearBean(year));
        return mapping.findForward("showDefinitionPeriod");
    }

    public ActionForward showPeriodWithSelectedYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IViewState viewState = RenderUtils.getViewState("executionYear");
        ExecutionYear executionYear = (ExecutionYear) viewState.getMetaObject().getObject();
        readAndSetPeriod(request, executionYear);
        request.setAttribute("bean", new ExecutionYearBean(executionYear));
        return mapping.findForward("showDefinitionPeriod");
    }

    public ActionForward showPeriodInExecutionYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionYear executionYear = getExecutionYearFromParameter(request);
        readAndSetPeriod(request, executionYear);
        request.setAttribute("bean", new ExecutionYearBean(executionYear));
        return mapping.findForward("showDefinitionPeriod");
    }

    public ActionForward createPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionYear executionYear = getExecutionYearFromParameter(request);
        request.setAttribute("executionYear", executionYear);
        request.setAttribute("department", getDepartment(request));
        return mapping.findForward("editDefinitionPeriod");
    }

    public ActionForward editPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionYear executionYear = getExecutionYearFromParameter(request);
        readAndSetPeriod(request, executionYear);
        return mapping.findForward("editDefinitionPeriod");
    }

    // Private Methods

    protected void readAndSetPeriod(HttpServletRequest request, ExecutionYear executionYear) {
        if (executionYear != null) {
            TeacherExpectationDefinitionPeriod teacherExpectationDefinitionPeriod =
                    getDepartment(request).getTeacherExpectationDefinitionPeriodForExecutionYear(executionYear);
            request.setAttribute("period", teacherExpectationDefinitionPeriod);
        }
    }

    protected ExecutionYear getExecutionYearFromParameter(final HttpServletRequest request) {
        final String executionYearIDString = request.getParameter("executionYearId");
        return FenixFramework.getDomainObject(executionYearIDString);
    }

    protected Department getDepartment(HttpServletRequest request) {
        return getLoggedPerson(request).getEmployee().getCurrentDepartmentWorkingPlace();
    }
}
