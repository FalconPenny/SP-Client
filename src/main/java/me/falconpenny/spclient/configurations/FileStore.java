package me.falconpenny.spclient.configurations;

import lombok.NonNull;

import java.io.File;

public interface FileStore {
    @NonNull
    File file();
}
