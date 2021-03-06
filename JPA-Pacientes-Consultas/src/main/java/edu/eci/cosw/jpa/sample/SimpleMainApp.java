/*
 * Copyright (C) 2016 hcadavid
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.cosw.jpa.sample;

import edu.eci.cosw.jpa.sample.model.Consulta;
import edu.eci.cosw.jpa.sample.model.Paciente;
import edu.eci.cosw.jpa.sample.model.PacienteId;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * @author hcadavid
 */
public class SimpleMainApp {

    public static void main(String a[]) {
        SessionFactory sf = getSessionFactory();
        Session s = sf.openSession();
        Transaction tx = s.beginTransaction();

        // Consultando paciente
        PacienteId id1 = new PacienteId(1, "cc");
        Paciente paciente = (Paciente) s.load(Paciente.class, id1);
        System.out.println("ID: " + paciente.getId());
        System.out.println("Tipo ID: " + paciente.getId());
        System.out.println("Nombre: " + paciente.getNombre());
        System.out.println("Fecha Nacimiento: " + paciente.getFechaNacimiento());
        Set<Consulta> consultas = paciente.getConsultas();
        Iterator<Consulta> it = consultas.iterator();
        while (it.hasNext()) {
            System.out.println("Resumen: "+it.next().getResumen());
        }
        // Adicionando consulta al paciente consultado
        Date today = new Date();
        Consulta consulta1 = new Consulta(today,paciente.getNombre());
        paciente.getConsultas().add(consulta1);
        s.saveOrUpdate("PACIENTES", paciente);

        tx.commit();
        s.close();
        sf.close();

    }

    public static SessionFactory getSessionFactory() {
        // loads configuration and mappings
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        // builds a session factory from the service registry
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        return sessionFactory;
    }

}
