package de.isah.vocabtrainer.dictionary.persist;

import java.io.IOException;

import de.isah.vocabtrainer.logging.SwedishVocabAppLogger;

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
                SwedishVocabAppLogger.log("ih: persistence type json1", PersistenceFactory.class);
                persistence = new UserFilePersistenceJson("dictionaryjson.txt");
                break;
            case "2":
                SwedishVocabAppLogger.log("ih: persistence type app", PersistenceFactory.class);
                persistence = new AppFilePersistence();
                break;
            case "3":
                SwedishVocabAppLogger.log("ih: persistence type hardcoded", PersistenceFactory.class);
                persistence = new HardcodedValuesPersistence();
                break;
            case "4":
                SwedishVocabAppLogger.log("ih: persistence type json2", PersistenceFactory.class);
                persistence = new UserFilePersistenceJson("dictionaryjson2.txt");
                break;
            default:
                SwedishVocabAppLogger.log("ih: persistence type default (hardcoded)", PersistenceFactory.class);
                persistence = new HardcodedValuesPersistence();
        }

        return persistence;
    }
}
