create table refresh_tokens(
                               id UUID primary key,
                               token VARCHAR(255) unique,
                               user_id UUID,
                               session_id VARCHAR(255),
                               expiry_date TIMESTAMP default CURRENT_TIMESTAMP,

                               CONSTRAINT fk_refresh_token_user
                                   FOREIGN KEY (user_id) REFERENCES users(id)
)