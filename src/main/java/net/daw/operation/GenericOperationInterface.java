/*
 * Copyright (C) July 2014 Rafael Aznar
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.daw.operation;

import java.util.ArrayList;
import java.util.HashMap;
import net.daw.helper.FilterBean;

public interface GenericOperationInterface {

    public String get(Integer id) throws Exception;

    public String getPage(int intRegsPerPag, int intPage, ArrayList<FilterBean> alFilter, HashMap<String, String> hmOrder) throws Exception;

    public String getPages(int intRegsPerPag, ArrayList<FilterBean> alFilter) throws Exception;

    public String remove(Integer id) throws Exception;

    public String save(String jason) throws Exception;

    public String getColumns() throws Exception;

    public String getCount(ArrayList<FilterBean> alFilter) throws Exception;

    public String getPrettyColumns() throws Exception;

}
