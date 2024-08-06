CREATE TABLE translation_requests (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      ip_address VARCHAR(255),
                                      input_text VARCHAR(255),
                                      translated_text VARCHAR(255),
                                      lang_from VARCHAR(10),
                                      lang_to VARCHAR(10),
                                      request_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);