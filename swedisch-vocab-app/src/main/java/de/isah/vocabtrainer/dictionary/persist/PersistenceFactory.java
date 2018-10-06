package de.isah.vocabtrainer.dictionary.persist;

import java.io.IOException;

/**
 * Creates a Persistence object based on the persistence type setting
 *
 * @author isa.heinze
 */

public class PersistenceFactory {

    private Persistence persistence;

    public Persistence create(String persistenceType) throws IOException{

        if(persistence != null) {
            return persistence;
        }

        switch (persistenceType){
            case "1":
                persistence = new UserFilePersistence();
                break;
            case "2":
                persistence = new AppFilePersistence();
                break;
            case "3":
                persistence = new HardcodedValuesPersistence();
                break;
            default:
                persistence = new HardcodedValuesPersistence();
        }

        return persistence;
    }
}
