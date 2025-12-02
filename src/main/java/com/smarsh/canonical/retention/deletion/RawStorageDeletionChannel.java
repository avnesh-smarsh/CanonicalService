//package com.smarsh.canonical.retention.deletion;
//
//import com.smarsh.canonical.exception.retention.DeletionException;
//import com.smarsh.canonical.retention.client.RawStorageDeletionClient;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class RawStorageDeletionChannel implements DeletionChannel<String> {
//
//    private final RawStorageDeletionClient rawStorageDeletionClient;
//
//    @Override
//    public void delete(List<String> rawReferences) {
//        try {
//            rawStorageDeletionClient.deleteRawReferences(rawReferences);
//            log.info("Deleted {} raw references from Raw Storage", rawReferences.size());
//        } catch (Exception e) {
//            log.error("Error deleting raw references from Raw Storage : {}", e.getMessage());
//            throw new DeletionException("Failed to delete raw references from Raw Storage", e);
//        }
//    }
//
//}
