package seedu.splitlah.command;

import seedu.splitlah.data.Manager;
import seedu.splitlah.data.Person;
import seedu.splitlah.data.PersonList;
import seedu.splitlah.data.Profile;
import seedu.splitlah.data.Session;
import seedu.splitlah.exceptions.InvalidDataException;
import seedu.splitlah.ui.Message;
import seedu.splitlah.ui.TextUI;

import java.time.LocalDate;
import java.util.logging.Level;

/**
 * Represents a command which edits an existing Session object from a list of sessions managed by the Profile object.
 *
 * @author Roy
 */
public class SessionEditCommand extends Command {

    private static final String COMMAND_SUCCESS = "The session was edited successfully.";
    private static final String COMMAND_NO_EDITS_MADE = "The session was not edited.";

    private final int sessionId;
    private final String sessionName;
    private final String[] personNames;
    private final LocalDate sessionDate;

    /**
     * Initializes a SessionEditCommand object.
     *
     * @param sessionId   An integer that uniquely identifies a session.
     * @param sessionName A String object that represents the session name.
     * @param personNames An array of String objects that represents the involved persons for the session.
     * @param date        A LocalDate object that represents the date of the session.
     */
    public SessionEditCommand(int sessionId, String sessionName, String[] personNames, LocalDate date) {
        assert sessionId > 0 : Message.ASSERT_SESSIONEDIT_SESSION_ID_INVALID;
        this.sessionId = sessionId;
        this.sessionName = sessionName;
        this.personNames = personNames;
        this.sessionDate = date;
    }

    /**
     * Runs the command to edit an existing Session object from the list of sessions managed by a Manager Object.
     *
     * @param manager A Manager object that manages the TextUI, Profile and Storage object.
     */
    @Override
    public void run(Manager manager) {
        TextUI ui = manager.getUi();
        Profile profile = manager.getProfile();
        Session session;
        PersonList newPersonList = null;
        String newSessionName = null;
        try {
            session = profile.getSession(sessionId);
            if (personNames != null) {
                newPersonList = getNewPersonList(session.getPersonList());
            }
            if (sessionName != null) {
                newSessionName = getNewSessionName(session.getSessionName(), profile);
            }
            if (sessionDate != null) {
                session.setDateCreated(sessionDate);
            }
        } catch (InvalidDataException invalidDataException) {
            ui.printlnMessageWithDivider(invalidDataException.getMessage());
            return;
        }
        boolean isSessionEdited = hasSessionEdited(session);
        if (newPersonList != null) {
            for (Person person : newPersonList.getPersonList()) {
                session.addPerson(person);
            }
        }
        if (newSessionName != null) {
            session.setSessionName(newSessionName);
        }
        if (isSessionEdited) {
            ui.printlnMessageWithDivider(COMMAND_SUCCESS);
        } else {
            ui.printlnMessageWithDivider(COMMAND_NO_EDITS_MADE);
        }
        manager.saveProfile();
        Manager.getLogger().log(Level.FINEST, Message.LOGGER_SESSIONEDIT_SESSION_EDITED);
    }
}
