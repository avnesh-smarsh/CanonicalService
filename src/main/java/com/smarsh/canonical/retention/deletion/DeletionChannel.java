package com.smarsh.canonical.retention.deletion;

import java.util.List;

public interface DeletionChannel<T> {
    void delete(List<T> items);
}
