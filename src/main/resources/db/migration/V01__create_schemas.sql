CREATE TABLE POLICY(
    ID BIGSERIAL,
    ID_EXTERNAL UUID NOT NULL UNIQUE default gen_random_uuid(),
    CUSTOMER_ID UUID NOT NULL,
    PRODUCT_ID UUID NOT NULL,
    CATEGORY VARCHAR(255) NOT NULL,
    SALES_CHANNEL VARCHAR(255) NOT NULL,
    PAYMENT_METHOD VARCHAR(255) NOT NULL,
    PAYMENT_PHASE VARCHAR(255) NOT NULL,
    SUBSCRIPTION_AUTHORIZATION_PHASE VARCHAR(255) NOT NULL,
    POLICY_SOLICITATION VARCHAR(255) NOT NULL,
    TOTAL_MONTHLY_PREMIUM_AMOUNT NUMERIC(7, 2) NOT NULL,
    INSURED_AMOUNT NUMERIC(7, 2) NOT NULL,
    CREATED_AT TIMESTAMP WITHOUT TIME ZONE NULL,
    FINISHED_AT TIMESTAMP WITHOUT TIME ZONE NULL,
    CONSTRAINT PK_POLICY_ID PRIMARY KEY(ID)
);

CREATE TABLE POLICY_COVERAGE(
    ID BIGSERIAL,
    TITLE VARCHAR(255) NOT NULL,
    AMOUNT NUMERIC(7, 2) NOT NULL,
    POLICY_ID BIGINT,
    CONSTRAINT POLICY_COVERAGE_ID PRIMARY KEY(ID),
    CONSTRAINT FK_POLICY_ID FOREIGN KEY(POLICY_ID) REFERENCES POLICY(ID)
);

CREATE TABLE ASSISTANCE(
    ID BIGSERIAL,
    ASSISTANCE VARCHAR(255) NOT NULL,
    POLICY_ID BIGINT,
    CONSTRAINT ASSISTANCE_ID PRIMARY KEY(ID),
    CONSTRAINT FK_POLICY_ID FOREIGN KEY(POLICY_ID) REFERENCES POLICY(ID)
);

CREATE TABLE POLICY_HISTORY(
    ID BIGSERIAL,
    STATUS VARCHAR(255) NOT NULL,
    CREATED_AT TIMESTAMP WITHOUT TIME ZONE NULL,
    POLICY_ID BIGINT,
    CONSTRAINT POLICY_HISTORY_ID PRIMARY KEY(ID),
    CONSTRAINT FK_POLICY_ID FOREIGN KEY(POLICY_ID) REFERENCES POLICY(ID)
);