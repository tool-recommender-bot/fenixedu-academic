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
package net.sourceforge.fenixedu.domain.person;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.struts.util.LabelValueBean;

public class GenderHelper {

    public static final String GENDER_RESOURCE_BUNDLE = "resources.ApplicationResources";

    private GenderHelper() {
        super();
    }

    private static final Map<Locale, LabelValueBean[]> sexLabelValuesByLocale = new HashMap<Locale, LabelValueBean[]>(2);

    public static LabelValueBean[] getSexLabelValues(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        LabelValueBean[] labelValueBeans = sexLabelValuesByLocale.get(locale);
        if (labelValueBeans != null) {
            return labelValueBeans;
        }

        final ResourceBundle resourceBundle = ResourceBundle.getBundle(GENDER_RESOURCE_BUNDLE, locale);
        labelValueBeans =
                new LabelValueBean[] { new LabelValueBean(resourceBundle.getString(Gender.MALE.name()), Gender.MALE.name()),
                        new LabelValueBean(resourceBundle.getString(Gender.FEMALE.name()), Gender.FEMALE.name()) };
        sexLabelValuesByLocale.put(locale, labelValueBeans);
        return labelValueBeans;
    }

    public static String toLocalizedString(Gender gender, Locale locale) {
        try {
            final ResourceBundle resourceBundle = ResourceBundle.getBundle(GENDER_RESOURCE_BUNDLE, locale);
            return resourceBundle.getString(gender.name());
        } catch (Exception swallowed) {
            return null;
        }
    }

    public static String toLocalizedString(Gender gender) {
        return GenderHelper.toLocalizedString(gender, Locale.getDefault());
    }

    public static Gender parseGender(String name) {
        for (Gender g : Gender.values()) {
            if (g.name().equalsIgnoreCase(name)) {
                return g;
            }
        }

        return null;
    }

}
