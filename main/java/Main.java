import org.junit.platform.launcher.listeners.TestExecutionSummary;
import view.InvalidDataException;
import view.View;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) throws InvalidDataException {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
        EntityManager manager = factory.createEntityManager();


        View view = new View(manager);
        view.runProgram();

    }
}