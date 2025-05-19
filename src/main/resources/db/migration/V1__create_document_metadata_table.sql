CREATE TABLE document_metadata (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    document_id VARCHAR(255) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    edinet_code VARCHAR(255) NOT NULL,
    document_type INT NOT NULL,
    form_code VARCHAR(50) NOT NULL,
    submission_date DATE NOT NULL,
    processed BOOLEAN DEFAULT FALSE NOT NULL
);
