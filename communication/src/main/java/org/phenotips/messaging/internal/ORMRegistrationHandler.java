/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/
 */
package org.phenotips.messaging.internal;

import org.phenotips.messaging.Connection;

import org.xwiki.component.annotation.Component;
import org.xwiki.observation.EventListener;
import org.xwiki.observation.event.ApplicationStartedEvent;
import org.xwiki.observation.event.Event;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.xpn.xwiki.store.hibernate.HibernateSessionFactory;

/**
 * Registers the {@link Connection connection entity} in the Hibernate configuration at startup, since Hibernate doesn't
 * have a clean mechanism for auto-registering optional modular entities at runtime.
 *
 * @version $Id$
 * @since 1.0M1
 */
@Component
@Named("phenotips-messaging-orm-registration")
@Singleton
public class ORMRegistrationHandler implements EventListener
{
    /** The Hibernate session factory where the entity must be registered. */
    @Inject
    private HibernateSessionFactory sessionFactory;

    @Override
    public String getName()
    {
        return "phenotips-messaging-orm-registration";
    }

    @Override
    public List<Event> getEvents()
    {
        return Collections.<Event>singletonList(new ApplicationStartedEvent());
    }

    @Override
    public void onEvent(Event event, Object source, Object data)
    {
        this.sessionFactory.getConfiguration().addAnnotatedClass(Connection.class);
    }
}
