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
 * Created on 20/Fev/2004
 */
package net.sourceforge.fenixedu.util;

import java.io.Serializable;

import org.apache.commons.lang.enums.ValuedEnum;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 20/Fev/2004
 * 
 */
public abstract class FenixValuedEnum extends ValuedEnum implements Serializable {

    /**
     * @param arg0
     * @param arg1
     */
    public FenixValuedEnum(String arg0, int arg1) {
        super(arg0, arg1);
    }

}