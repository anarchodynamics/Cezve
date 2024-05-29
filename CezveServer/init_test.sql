-- Create Users table
CREATE TABLE IF NOT EXISTS Users (
    UserID INTEGER PRIMARY KEY,
    User TEXT NOT NULL,
    Password TEXT NOT NULL,
    UserIsAdmin BOOLEAN,
    TopLevelDirectoryPath TEXT NOT NULL
);

-- Create Files table
CREATE TABLE IF NOT EXISTS Files (
    FileID INTEGER PRIMARY KEY,
    FileOwner INTEGER NOT NULL,
    FilePath TEXT NOT NULL,
    FOREIGN KEY(FileOwner) REFERENCES Users(UserID)
);

-- Create FileSharing table
CREATE TABLE IF NOT EXISTS FileSharing (
    ShareID INTEGER PRIMARY KEY,
    FileShared INTEGER NOT NULL,
    FileSharedByUser INTEGER NOT NULL,
    FileSharedToUser INTEGER NOT NULL,
    FOREIGN KEY(FileShared) REFERENCES Files(FileID),
    FOREIGN KEY(FileSharedByUser) REFERENCES Users(UserID),
    FOREIGN KEY(FileSharedToUser) REFERENCES Users(UserID),
    CHECK (FileSharedByUser != FileSharedToUser)
);
