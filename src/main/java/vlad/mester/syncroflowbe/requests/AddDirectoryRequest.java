package vlad.mester.syncroflowbe.requests;

import lombok.Data;

@Data
public class AddDirectoryRequest {
    private String directory;
    private String parentDirectory;
}