package grails.plugin.opencmis

import org.apache.chemistry.opencmis.client.api.Session

class SessionHolderService {

	def grailsApplication
	def repositoryHandlerService

    private static final String DEFAULT = 'default'
    private static final Object LOCK = new Object()
    // singleton
    private static final SessionHolderService INSTANCE = new SessionHolderService()

    private final Map<String, Session> sessions = [:]

    static SessionHolderService getInstance() {
        INSTANCE
    }

    String[] getSessionNames() {
        sessions.keySet() as String[]
    }

    Session getSession(String sessionName = DEFAULT) {
        retrieveSession(sessionName)
    }

    void setSession(String sessionName = DEFAULT, Session session) {
        storeSession(sessionName, session)
    }

    boolean isSessionConnected(String sessionName) {
        retrieveSession(sessionName) != null
    }

    void disconnectSession(String sessionName) {
        storeSession(sessionName, null)
    }

    Session fetchSession(String sessionName) {
        Session session = retrieveSession(sessionName)
        if (session == null) {
            session = repositoryHandlerService.instance.connect(sessionName)
        }

        if (session == null) {
            throw new IllegalArgumentException("No such Session configuration for name $sessionName")
        }
        session
    }

    private Session retrieveSession(String sessionName) {
        if (!sessionName) sessionName = DEFAULT
        synchronized(LOCK) {
            sessions[sessionName]
        }
    }

    private void storeSession(String sessionName, Session session) {
        if (!sessionName) sessionName = DEFAULT
        synchronized(LOCK) {
            sessions[sessionName] = session
        }
    }
}
