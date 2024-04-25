package Saturday_CSS217;

import java.util.HashMap;
import java.util.Map;

interface DocumentStorage {
    void uploadDocument(String documentName, byte[] documentContent);
    byte[] downloadDocument(String documentName);
}

class RealDocumentStorage implements DocumentStorage {
    private Map<String, byte[]> documents = new HashMap<>();

    public void uploadDocument(String documentName, byte[] documentContent) {
        documents.put(documentName, documentContent);
    }

    public byte[] downloadDocument(String documentName) {
        return documents.get(documentName);
    }
}
class DocumentStorageProxy implements DocumentStorage {
    private DocumentStorage realStorage;
    private Map<String, byte[]> cachedDocuments = new HashMap<>();

    public DocumentStorageProxy() {
        this.realStorage = new RealDocumentStorage();
    }

    public void uploadDocument(String documentName, byte[] documentContent) {
        if (checkUserPermissions()) {
            realStorage.uploadDocument(documentName, documentContent);
            logUserActivity("Uploaded document: " + documentName);
        } else {
            System.out.println("Access denied. User does not have permission to upload documents.");
        }
    }

    public byte[] downloadDocument(String documentName) {
        if (checkUserPermissions()) {
            if (cachedDocuments.containsKey(documentName)) {
                System.out.println("Document '" + documentName + "' found in cache.");
                return cachedDocuments.get(documentName);
            } else {
                byte[] documentContent = realStorage.downloadDocument(documentName);
                documentContent = transformDocumentContent(documentContent);
                cachedDocuments.put(documentName, documentContent);
                logUserActivity("Downloaded document: " + documentName);
                return documentContent;
            }
        } else {
            System.out.println("Access denied. User does not have permission to download documents.");
            return null;
        }
    }

    private boolean checkUserPermissions() {
        return true;
    }

    private void logUserActivity(String activity) {
        System.out.println("User activity logged: " + activity);
    }

    private byte[] transformDocumentContent(byte[] documentContent) {
        return documentContent;
    }
}
public class Main {
    public static void main(String[] args) {
        DocumentStorageProxy documentStorage = new DocumentStorageProxy();

        documentStorage.uploadDocument("Document1", "Content of Document1".getBytes());
        documentStorage.downloadDocument("Document1");
        documentStorage.downloadDocument("Document2");
    }
}
