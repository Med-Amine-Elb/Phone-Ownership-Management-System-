-- Add deleted column to phone_assignments table
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID(N'[dbo].[phone_assignments]') AND name = 'deleted')
BEGIN
    ALTER TABLE [dbo].[phone_assignments] ADD deleted BIT NOT NULL DEFAULT 0;
END 