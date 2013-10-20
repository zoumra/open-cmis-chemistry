package grails.plugin.opencmis

import org.apache.chemistry.opencmis.client.api.Session

class SessionHolderService {

	def grailsApplication
	def repositoryHandlerService

    private static final String DEFAULT = 'default'
    private static final Object[] LOCK = new Object[0]
    private static final SessionHolderService INSTANCE

    private final Map<String, Session> sessions = [:]
    
    // singleton
    static {
        INSTANCE = new SessionHolderService()
    }

    static SessionHolderService getInstance() {
        INSTANCE
    }

    String[] getSessionNames() {
        List<String> sessionNames = new ArrayList().addAll(sessions.keySet())
        sessionNames.toArray(new String[sessionNames.size()])
    }

    Session getSession(String sessionName = DEFAULT) {
        if (isBlank(sessionName)) sessionName = DEFAULT
        retrieveSession(sessionName)
    }

    void setSession(String sessionName = DEFAULT, Session session) {
        if (isBlank(sessionName)) sessionName = DEFAULT
        storeSession(sessionName, session)
    }

    boolean isSessionConnected(String sessionName) {
        if (isBlank(sessionName)) sessionName = DEFAULT
        retrieveSession(sessionName) != null
    }

    void disconnectSession(String sessionName) {
        if (isBlank(sessionName)) sessionName = DEFAULT
        storeSession(sessionName, null)
    }

    Session fetchSession(String sessionName) {
        if (isBlank(sessionName)) sessionName = DEFAULT
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
        synchronized(LOCK) {
            sessions[sessionName]
        }
    }

    private void storeSession(String sessionName, Session session) {
        synchronized(LOCK) {
            sessions[sessionName] = session
        }
    }

}
