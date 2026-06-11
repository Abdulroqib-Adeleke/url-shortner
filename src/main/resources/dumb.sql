
CREATE TABLE users(
    id UUID primary key,
    name VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    created_date TIMESTAMP default CURRENT_TIMESTAMP,
    updated_date TIMESTAMP default  CURRENT_TIMESTAMP
)

CREATE TABLE user_roles(
    user_id UUID NOT NULL,
    role VARCHAR(50) NOT NULL,
    CONSTRAINT fk_user_roles_user_id
        FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT pk_user_roles
        PRIMARY KEY (user_id, role)
)

CREATE TABLE user_login_session(
    id UUID primary key,
    active_session_id VARCHAR(255) not null,
    user_id UUID not null,
    created_date TIMESTAMP default CURRENT_TIMESTAMP,
    updated_date TIMESTAMP default  CURRENT_TIMESTAMP,

    CONSTRAINT fk_login_session_user_id
        FOREIGN KEY(user_id) REFERENCES users(id)
)

alter table users add column user_type varchar(50) default  'CUSTOMER';

CREATE  TABLE company(
                         id UUID primary key,
                         admin_id UUID NOT NULL,
                         support_email VARCHAR(255),
                         name VARCHAR(255),
                         created_date TIMESTAMP default CURRENT_TIMESTAMP,
                         updated_date TIMESTAMP default  CURRENT_TIMESTAMP,
                         CONSTRAINT fk_company_admin_id
                             FOREIGN KEY(admin_id) REFERENCES users(id)
)

CREATE TABLE principal_service_offering(
                                           id UUID primary key,
                                           service VARCHAR(50) NOT NULL,
                                           active boolean DEFAULT true,
                                           created_date TIMESTAMP default CURRENT_TIMESTAMP,
                                           updated_date TIMESTAMP default  CURRENT_TIMESTAMP,
)

CREATE TABLE agent_service_offering(
                                       id UUID primary key,
                                       principal_service_id UUID NOT NULL,
                                       active boolean DEFAULT  true,
                                       company_id UUID NOT NULL,
                                       created_date TIMESTAMP default CURRENT_TIMESTAMP,
                                       updated_date TIMESTAMP default  CURRENT_TIMESTAMP,

                                       CONSTRAINT fk_agent_service_psi
                                           FOREIGN KEY(principal_service_id) REFERENCES principal_service_offering(id),
                                       CONSTRAINT fk_agent_service_ci
                                           FOREIGN KEY(company_id) REFERENCES company(id),
)

CREATE TABLE customers(
                          id UUID primary key,
                          name VARCHAR(255),
                          email VARCHAR(255),
                          user_id UUID,
                          company_id UUID,
                          created_date TIMESTAMP default CURRENT_TIMESTAMP,
                          updated_date TIMESTAMP default  CURRENT_TIMESTAMP,

                          CONSTRAINT fk_customer_user_id
                              FOREIGN KEY(user_id) REFERENCES users(id),

                          CONSTRAINT fk_customer_company_id
                              FOREIGN KEY(company_id) REFERENCES company(id)
)

CREATE TABLE enable_service_request(
                                       id UUID primary key,
                                       service_id UUID,
                                       admin_id UUID,
                                       service_request_status VARCHAR(255),
                                       created_date TIMESTAMP default CURRENT_TIMESTAMP,
                                       updated_date TIMESTAMP default  CURRENT_TIMESTAMP,

                                       CONSTRAINT fk_ear_service_id
                                           FOREIGN KEY(service_id) REFERENCES agent_service_offering(id),

                                       CONSTRAINT fk_admin_id
                                           FOREIGN KEY(admin_id) REFERENCES users(id)
)


