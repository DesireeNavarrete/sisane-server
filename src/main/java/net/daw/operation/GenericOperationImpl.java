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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import net.daw.bean.GenericBeanImpl;
import net.daw.bean.GenericBeanInterface;
import net.daw.dao.GenericDaoImpl;
import net.daw.helper.FilterBean;

public abstract class GenericOperationImpl implements GenericOperationInterface {

    protected Connection oConnection = null;
    protected String strObjectName = null;

    public GenericOperationImpl(String ob, Connection con) {
        strObjectName = Character.toUpperCase(ob.charAt(0)) + ob.substring(1);
        oConnection = con;
    }

    @Override
    public String getPrettyColumns() throws Exception {
        try {
            oConnection.setAutoCommit(false);
            Constructor c = Class.forName("net.daw.dao." + strObjectName + "Dao").getConstructor(Connection.class);
            GenericDaoImpl oDao = (GenericDaoImpl) c.newInstance(oConnection);
            ArrayList<String> alColumns = null;
            String data;
            alColumns = oDao.getPrettyColumnsNames();
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            data = gson.toJson(alColumns);
            return data;
        } catch (Exception e) {
            throw new ServletException("GetJson: View Error: " + e.getMessage());
        } finally {
            oConnection.commit();
        }
    }

    @Override
    public String getColumns() throws Exception {
        try {
            oConnection.setAutoCommit(false);
            Constructor c = Class.forName("net.daw.dao." + strObjectName + "Dao").getConstructor(Connection.class);
            GenericDaoImpl oDao = (GenericDaoImpl) c.newInstance(oConnection);
            ArrayList<String> alColumns = null;
            String data;
            alColumns = oDao.getColumnsNames();
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            data = gson.toJson(alColumns);
            return data;
        } catch (Exception e) {
            throw new ServletException("GetJson: View Error: " + e.getMessage());
        } finally {
            oConnection.commit();
        }
    }

    @Override
    public String get(Integer id) throws Exception {
        try {
            String data;
            oConnection.setAutoCommit(false);
            GenericBeanImpl oGenericBean = (GenericBeanImpl) Class.forName("net.daw.bean." + strObjectName + "Bean").newInstance();
            Constructor c = Class.forName("net.daw.dao." + strObjectName + "Dao").getConstructor(Connection.class);
            GenericDaoImpl oGenericDao = (GenericDaoImpl) c.newInstance(oConnection);
            oGenericBean.setId(id);
            oGenericBean = (GenericBeanImpl) (GenericBeanInterface) oGenericDao.get(oGenericBean);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("dd/MM/yyyy");
            Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
            data = gson.toJson(oGenericBean);
            return data;
        } catch (Exception e) {
            throw new ServletException("GetJson: View Error: " + e.getMessage());
        } finally {
            oConnection.commit();
        }
    }

    @Override
    public String getPage(int intRegsPerPag, int intPage, ArrayList<FilterBean> alFilter, HashMap<String, String> hmOrder) throws Exception {
        try {
            oConnection.setAutoCommit(false);
            GenericBeanImpl oGenericBean = (GenericBeanImpl) Class.forName("net.daw.bean." + strObjectName + "Bean").newInstance();
            Constructor c = Class.forName("net.daw.dao." + strObjectName + "Dao").getConstructor(Connection.class);
            GenericDaoImpl oGenericDao = (GenericDaoImpl) c.newInstance(oConnection);
            List<GenericBeanInterface> loGenericBean = oGenericDao.getPage(intRegsPerPag, intPage, alFilter, hmOrder);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("dd/MM/yyyy");
            Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
            String data = gson.toJson(loGenericBean);
            data = "{\"list\":" + data + "}";
            return data;
        } catch (Exception e) {
            throw new ServletException("GetpageJson: View Error: " + e.getMessage());
        } finally {
            oConnection.commit();
        }
    }

    @Override
    public String getPages(int intRegsPerPag, ArrayList<FilterBean> alFilter) throws Exception {
        try {
            oConnection.setAutoCommit(false);
            Constructor c = Class.forName("net.daw.dao." + strObjectName + "Dao").getConstructor(Connection.class);
            GenericDaoImpl oGenericDao = (GenericDaoImpl) c.newInstance(oConnection);
            int pages = oGenericDao.getPages(intRegsPerPag, alFilter);
            String data = "{\"data\":\"" + Integer.toString(pages) + "\"}";
            return data;
        } catch (Exception e) {
            throw new ServletException("FollowerGetpagesJson: View Error: " + e.getMessage());
        } finally {
            oConnection.commit();
        }
    }

    @Override
    public String remove(Integer id) throws Exception {
        try {
            oConnection.setAutoCommit(false);
            GenericBeanImpl oGenericBean = (GenericBeanImpl) Class.forName("net.daw.bean." + strObjectName + "Bean").newInstance();
            Constructor c = Class.forName("net.daw.dao." + strObjectName + "Dao").getConstructor(Connection.class);
            GenericDaoImpl oGenericDao = (GenericDaoImpl) c.newInstance(oConnection);
            oGenericBean.setId(id);
            Map<String, String> data = new HashMap<>();
            if (oGenericBean != null) {
                oGenericDao.remove(oGenericBean);
                data.put("status", "200");
                data.put("message", "se ha eliminado el registro con id=" + oGenericBean.getId());
            } else {
                data.put("status", "error");
                data.put("message", "error");
            }
            Gson gson = new Gson();
            String resultado = gson.toJson(data);
            return resultado;
        } catch (Exception e) {
            throw new ServletException("RemoveJson: View Error: " + e.getMessage());
        } finally {
            oConnection.commit();
        }
    }

    @Override
    public String save(String jason) throws Exception {
        try {
            oConnection.setAutoCommit(false);
            GenericBeanImpl oGenericBean = (GenericBeanImpl) Class.forName("net.daw.bean." + strObjectName + "Bean").newInstance();
            Constructor c = Class.forName("net.daw.dao." + strObjectName + "Dao").getConstructor(Connection.class);
            GenericDaoImpl oGenericDao = (GenericDaoImpl) c.newInstance(oConnection);
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            oGenericBean = gson.fromJson(jason, oGenericBean.getClass());
            Map<String, String> data = new HashMap<>();
            if (oGenericBean != null) {
                oGenericBean = (GenericBeanImpl) (GenericBeanInterface) oGenericDao.set(oGenericBean);
                data.put("status", "200");
                data.put("message", Integer.toString(oGenericBean.getId()));
            } else {
                data.put("status", "error");
                data.put("message", "error");
            }
            String resultado = gson.toJson(data);
            return resultado;
        } catch (Exception e) {
            throw new ServletException("SaveJson: View Error: " + e.getMessage());
        } finally {
            oConnection.commit();
        }

    }

    @Override
    public String getCount(ArrayList<FilterBean> alFilter) throws Exception {
        try {
            oConnection.setAutoCommit(false);
            Constructor c = Class.forName("net.daw.dao." + strObjectName + "Dao").getConstructor(Connection.class);
            GenericDaoImpl oGenericDao = (GenericDaoImpl) c.newInstance(oConnection);
            int registers = oGenericDao.getCount(alFilter);
            String data = "{\"data\":\"" + Integer.toString(registers) + "\"}";
            return data;
        } catch (Exception e) {
            throw new ServletException("GetregistersJson: View Error: " + e.getMessage());
        } finally {
            oConnection.commit();
        }
    }

//    @Override
//    //no se utiliza por ahora
//    public String getList(int intRegsPerPag, int intPage, ArrayList<FilterBean> alFilter, HashMap<String, String> hmOrder, OPERATION_BEAN oBean, OPERATION_DAO oDao) throws Exception {
//        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        try {
//            //falta controlar la transacción a esta altura
//            String columns = this.getColumns();
//            String prettyColumns = this.getPrettyColumns();
//            //String types = this.getTypes();
//            String page = this.getPage(intRegsPerPag, intPage, alFilter, hmOrder, oBean, oDao);
//            String pages = this.getPages(intRegsPerPag, alFilter, oDao);
//            String registers = this.getRegisters(alFilter, oDao);
//            String data = "{\"data\":{"
//                    + "\"columns\":" + columns
//                    + ",\"prettyColumns\":" + prettyColumns
//                    // + ",\"types\":" + types
//                    + ",\"page\":" + page
//                    + ",\"pages\":" + pages
//                    + ",\"registers\":" + registers
//                    + "}}";
//            return data;
//        } catch (Exception e) {
//            throw new ServletException("GetpageJson: View Error: " + e.getMessage());
//        }
//    }
}
