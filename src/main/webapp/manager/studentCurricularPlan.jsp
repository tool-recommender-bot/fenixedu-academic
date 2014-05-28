<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/><%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %><%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %><%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %><%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%><%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %><h2><bean:message bundle="MANAGER_RESOURCES" key="link.manager.studentsManagement"/></h2><br /><p><span class="error"><!-- Error messages go here --><html:errors /></span></p>
<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
</logic:messagesPresent><jsp:include page="studentCurricularPlanHeader.jsp"/>	<bean:define id="studentNumber" name="studentCurricularPlanForm" property="number"/>	<bean:define id="degreeType" name="studentCurricularPlanForm" property="degreeType"/>	<html:link module="/manager" page="<%= "/studentsManagement.do?method=prepareCreateStudentCurricularPlan&amp;page=0&amp;number="				+ pageContext.findAttribute("studentNumber").toString()				+ "&amp;degreeType="				+ pageContext.findAttribute("degreeType").toString()			%>">		<bean:message bundle="MANAGER_RESOURCES" key="link.create.student.curricular.plan"/>	</html:link>
	
	<br/><br/>
	<html:link page="/createClassificationsForStudents.do?method=prepare"><bean:message key="button.createClassifications" bundle="MANAGER_RESOURCES" /></html:link>
	<br/>	<logic:present name="infoStudentCurricularPlans">		<br/>		<bean:define id="deleteConfirmStudentCurricularPlan">			return confirm('<bean:message bundle="MANAGER_RESOURCES" key="message.confirm.delete.studentCurricularPlan"/>')		</bean:define>		<bean:define id="deleteConfirmEnrollment">			return confirm('<bean:message bundle="MANAGER_RESOURCES" key="message.confirm.delete.enrollment"/>')		</bean:define>				<logic:iterate id="infoStudentCurricularPlan" name="infoStudentCurricularPlans">			<bean:define id="studentCurricularPlanId" name="infoStudentCurricularPlan" property="externalId"/>			<bean:define id="studentNumber" name="infoStudentCurricularPlan" property="infoStudent.number"/>			<bean:define id="degreeType" name="infoStudentCurricularPlan" property="infoStudent.degreeType"/>
			
			<strong>
				<bean:write name="infoStudentCurricularPlan" property="studentCurricularPlan.registration.student.person.name" /> - 
				# <bean:write name="infoStudentCurricularPlan" property="studentCurricularPlan.registration.number" />
				(ID:<bean:write name="infoStudentCurricularPlan" property="studentCurricularPlan.registration.externalId" />)
			</strong>			<table>				<tr>					<th colspan="3" rowspan="3" class="listClasses-header">						<bean:message bundle="MANAGER_RESOURCES" key="label.studentCurricularPlan"/>					</th>					<td colspan="4" class="listClasses">						<bean:define id="studentCurricularPlanStateString" type="java.lang.String" name="infoStudentCurricularPlan" property="currentState.name"/>						<bean:define id="onChangeString" type="java.lang.String">this.form.method.value='changeStudentCurricularPlanState';this.form.selectedStudentCurricularPlanId.value=<%= studentCurricularPlanId.toString() %>;this.form.submit();</bean:define>						<logic:present name="infoStudentCurricularPlan" property="startDate">							<dt:format pattern="yyyy-MM-dd">								<bean:write name="infoStudentCurricularPlan" property="startDate.time"/>							</dt:format>						<bean:define id="studentCurricularPlanID" type="java.lang.String" name="infoStudentCurricularPlan" property="externalId"/>						<% net.sourceforge.fenixedu.domain.StudentCurricularPlan studentCurricularPlan = pt.ist.fenixframework.FenixFramework.getDomainObject(studentCurricularPlanID);						   request.setAttribute("studentCurricularPlan", studentCurricularPlan);						%>						<bean:define id="urlR" type="java.lang.String">/studentsManagement.do?method=show&page=0&number=<bean:write name="studentCurricularPlan" property="student.number"/>&degreeType=<bean:write name="studentCurricularPlan" property="degreeCurricularPlan.degree.tipoCurso"/></bean:define>						<fr:edit name="studentCurricularPlan"								type="net.sourceforge.fenixedu.domain.StudentCurricularPlan"								schema="student.curricular.plan.start.date"								action="<%= urlR %>"								>							<fr:layout name="tabular">								<fr:property name="classes" value="style1" />								<fr:property name="columnClasses" value="listClasses" />							</fr:layout>						</fr:edit>						</logic:present>											</td>					<td colspan="1" rowspan="3" class="listClasses">						<html:link module="/manager" page="<%= "/studentsManagement.do?method=deleteStudentCurricularPlan&amp;page=0&amp;number="									+ pageContext.findAttribute("studentNumber").toString()									+ "&amp;degreeType="									+ pageContext.findAttribute("degreeType").toString()									+ "&amp;studentCurricularPlanId="									+ pageContext.findAttribute("studentCurricularPlanId").toString()								%>"								onclick='<%= pageContext.findAttribute("deleteConfirmStudentCurricularPlan").toString() %>'>							<bean:message bundle="MANAGER_RESOURCES" key="link.delete"/>						</html:link>					</td>				</tr>				<tr>					<td  colspan="4" class="listClasses">						<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>					</td>				</tr>				<tr>					<td colspan="4" class="listClasses">						<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.name"/>						:						<logic:present name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.initialDate">							<dt:format pattern="yyyy-MM-dd">								<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.initialDate.time"/>							</dt:format>						</logic:present>						<logic:notPresent name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.initialDate">							...						</logic:notPresent>						-						<logic:present name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.endDate">							<dt:format pattern="yyyy-MM-dd">								<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.endDate.time"/>							</dt:format>						</logic:present>						<logic:notPresent name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.endDate">							...						</logic:notPresent>
						
						<logic:equal name="infoStudentCurricularPlan" property="studentCurricularPlan.boxStructure" value="true">
							<strong>BOX STRUCTURE</strong>	
						</logic:equal>					</td>				</tr>					<tr>						<th class="listClasses-header">						</th>						<th class="listClasses-header">							<bean:message bundle="MANAGER_RESOURCES" key="label.executionYear"/>						</th>						<th class="listClasses-header">							<bean:message bundle="MANAGER_RESOURCES" key="label.manager.semester"/>						</th>						<th class="listClasses-header">							<bean:message bundle="MANAGER_RESOURCES" key="label.manager.degree"/>						</th>						<th class="listClasses-header">							<bean:message bundle="MANAGER_RESOURCES" key="label.course.code"/>						</th>						<th class="listClasses-header">							<bean:message bundle="MANAGER_RESOURCES" key="label.course.name"/>						</th>						<th class="listClasses-header">							<bean:message bundle="MANAGER_RESOURCES" key="label.grade"/>						</th>						<th class="listClasses-header">						</th>					</tr><html:form action="/studentsManagement" focus="number">	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareTransferEnrollments"/>	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.number" property="number"/>	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeType" property="degreeType"/>	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedStudentCurricularPlanId" property="selectedStudentCurricularPlanId" value="<%= pageContext.findAttribute("studentCurricularPlanId").toString() %>"/>				<logic:iterate id="infoEnrollment" name="infoStudentCurricularPlan" property="infoEnrolmentsSorted">					<bean:define id="enrollmentId" name="infoEnrollment" property="externalId"/>					<bean:define id="enrollmentIdString" type="java.lang.String"><bean:write name="enrollmentId"/></bean:define>					<tr>						<td class="listClasses">							<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.enrollmentIDsToTransfer" property="enrollmentIDsToTransfer" value="<%= enrollmentIdString %>"/>						</td>						<td class="listClasses">							<bean:write name="infoEnrollment" property="infoExecutionPeriod.infoExecutionYear.year"/>						</td>						<td class="listClasses">							<bean:write name="infoEnrollment" property="infoExecutionPeriod.semester"/>						</td>						<td class="listClasses">							<bean:write name="infoEnrollment" property="infoCurricularCourse.infoDegreeCurricularPlan.name"/>						</td>						<td class="listClasses">							<bean:write name="infoEnrollment" property="infoCurricularCourse.code"/>						</td>						<td class="listClasses">							<bean:write name="infoEnrollment" property="infoCurricularCourse.name"/>						</td>						<td class="listClasses">							<bean:write name="infoEnrollment" property="gradeValue"/>						</td>						<td class="listClasses">							<html:link module="/manager" page="<%= "/studentsManagement.do?method=deleteEnrollment&amp;page=0&amp;studentNumber="										+ pageContext.findAttribute("studentNumber").toString()										+ "&amp;number="										+ pageContext.findAttribute("studentNumber").toString()										+ "&amp;degreeType="										+ pageContext.findAttribute("degreeType").toString()										+ "&amp;enrollmentId="										+ pageContext.findAttribute("enrollmentId").toString()									%>"									onclick='<%= pageContext.findAttribute("deleteConfirmEnrollment").toString() %>'>								<bean:message bundle="MANAGER_RESOURCES" key="link.delete"/>							</html:link>						</td>					</tr>				</logic:iterate>				<tr>					<th colspan="8" class="listClasses-header">						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" property="submit" styleClass="inputbutton"/>					</th>				</tr></html:form>			</table>			<br />		</logic:iterate>	</logic:present>