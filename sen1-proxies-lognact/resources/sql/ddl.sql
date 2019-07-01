--
-- PostgreSQL database dump
--
-- TOC entry 5 (class 2615 OID 21090)
-- Name: proxy; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA proxy;


ALTER SCHEMA proxy OWNER TO postgres;

--
-- TOC entry 1 (class 3079 OID 12655)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2453 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 186 (class 1259 OID 21091)
-- Name: app; Type: TABLE; Schema: proxy; Owner: postgres
--

CREATE TABLE proxy.app (
    id bigint NOT NULL,
    jid character varying(255) NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE proxy.app OWNER TO postgres;

--
-- TOC entry 2454 (class 0 OID 0)
-- Dependencies: 186
-- Name: TABLE app; Type: COMMENT; Schema: proxy; Owner: postgres
--

COMMENT ON TABLE proxy.app IS 'Apps';


--
-- TOC entry 187 (class 1259 OID 21097)
-- Name: config; Type: TABLE; Schema: proxy; Owner: postgres
--

CREATE TABLE proxy.config (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE proxy.config OWNER TO postgres;

--
-- TOC entry 2455 (class 0 OID 0)
-- Dependencies: 187
-- Name: TABLE config; Type: COMMENT; Schema: proxy; Owner: postgres
--

COMMENT ON TABLE proxy.config IS 'Config';


--
-- TOC entry 188 (class 1259 OID 21103)
-- Name: consumer; Type: TABLE; Schema: proxy; Owner: postgres
--

CREATE TABLE proxy.consumer (
    id bigint NOT NULL,
    unite character varying(255),
    date_last_value timestamp without time zone,
    user_app_id bigint NOT NULL,
    name character varying(255) NOT NULL,
    consumer_app_id bigint NOT NULL,
    metaname character varying(255),
    type character varying(255) NOT NULL,
    metavalue character varying(255),
    consumer_name character varying(255) NOT NULL,
    consumer_metaname character varying(255)
);


ALTER TABLE proxy.consumer OWNER TO postgres;

--
-- TOC entry 2456 (class 0 OID 0)
-- Dependencies: 188
-- Name: TABLE consumer; Type: COMMENT; Schema: proxy; Owner: postgres
--

COMMENT ON TABLE proxy.consumer IS 'Consumer';


--
-- TOC entry 189 (class 1259 OID 21109)
-- Name: inbox; Type: TABLE; Schema: proxy; Owner: postgres
--

CREATE TABLE proxy.inbox (
    id bigint NOT NULL,
    received_date timestamp without time zone NOT NULL,
    data bytea NOT NULL
);


ALTER TABLE proxy.inbox OWNER TO postgres;

--
-- TOC entry 2457 (class 0 OID 0)
-- Dependencies: 189
-- Name: TABLE inbox; Type: COMMENT; Schema: proxy; Owner: postgres
--

COMMENT ON TABLE proxy.inbox IS 'Inbox Data';


--
-- TOC entry 190 (class 1259 OID 21115)
-- Name: outbox; Type: TABLE; Schema: proxy; Owner: postgres
--

CREATE TABLE proxy.outbox (
    id bigint NOT NULL,
    received_date timestamp without time zone NOT NULL,
    data bytea NOT NULL
);


ALTER TABLE proxy.outbox OWNER TO postgres;

--
-- TOC entry 2458 (class 0 OID 0)
-- Dependencies: 190
-- Name: TABLE outbox; Type: COMMENT; Schema: proxy; Owner: postgres
--

COMMENT ON TABLE proxy.outbox IS 'Outbox Data';


--
-- TOC entry 191 (class 1259 OID 21121)
-- Name: user; Type: TABLE; Schema: proxy; Owner: postgres
--

CREATE TABLE proxy."user" (
    id bigint NOT NULL,
    username character varying(255)
);


ALTER TABLE proxy."user" OWNER TO postgres;

--
-- TOC entry 2459 (class 0 OID 0)
-- Dependencies: 191
-- Name: TABLE "user"; Type: COMMENT; Schema: proxy; Owner: postgres
--

COMMENT ON TABLE proxy."user" IS 'User';


--
-- TOC entry 192 (class 1259 OID 21124)
-- Name: user_app; Type: TABLE; Schema: proxy; Owner: postgres
--

CREATE TABLE proxy.user_app (
    id bigint NOT NULL,
    app_id bigint NOT NULL,
    user_id bigint NOT NULL,
    token character varying(255) NOT NULL
);


ALTER TABLE proxy.user_app OWNER TO postgres;

--
-- TOC entry 2460 (class 0 OID 0)
-- Dependencies: 192
-- Name: TABLE user_app; Type: COMMENT; Schema: proxy; Owner: postgres
--

COMMENT ON TABLE proxy.user_app IS 'UserApp';


--
-- TOC entry 193 (class 1259 OID 21127)
-- Name: sqlachemy_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sqlachemy_sequence
    START WITH 28
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sqlachemy_sequence OWNER TO postgres;

--
-- TOC entry 2299 (class 2606 OID 21130)
-- Name: app app_pkey; Type: CONSTRAINT; Schema: proxy; Owner: postgres
--

ALTER TABLE ONLY proxy.app
    ADD CONSTRAINT app_pkey PRIMARY KEY (id);


--
-- TOC entry 2304 (class 2606 OID 21132)
-- Name: config config_pkey; Type: CONSTRAINT; Schema: proxy; Owner: postgres
--

ALTER TABLE ONLY proxy.config
    ADD CONSTRAINT config_pkey PRIMARY KEY (id);


--
-- TOC entry 2308 (class 2606 OID 21134)
-- Name: consumer consumer_pkey; Type: CONSTRAINT; Schema: proxy; Owner: postgres
--

ALTER TABLE ONLY proxy.consumer
    ADD CONSTRAINT consumer_pkey PRIMARY KEY (id);


--
-- TOC entry 2312 (class 2606 OID 21136)
-- Name: inbox inbox_pkey; Type: CONSTRAINT; Schema: proxy; Owner: postgres
--

ALTER TABLE ONLY proxy.inbox
    ADD CONSTRAINT inbox_pkey PRIMARY KEY (id);


--
-- TOC entry 2314 (class 2606 OID 21138)
-- Name: outbox outbox_pkey; Type: CONSTRAINT; Schema: proxy; Owner: postgres
--

ALTER TABLE ONLY proxy.outbox
    ADD CONSTRAINT outbox_pkey PRIMARY KEY (id);


--
-- TOC entry 2310 (class 2606 OID 21140)
-- Name: consumer uk3984c55af281b70a60d912688d2c; Type: CONSTRAINT; Schema: proxy; Owner: postgres
--

ALTER TABLE ONLY proxy.consumer
    ADD CONSTRAINT uk3984c55af281b70a60d912688d2c UNIQUE (metavalue, metaname, name, user_app_id);


--
-- TOC entry 2321 (class 2606 OID 21142)
-- Name: user_app uk5fe3e526a17b0e3825b9a5d09218; Type: CONSTRAINT; Schema: proxy; Owner: postgres
--

ALTER TABLE ONLY proxy.user_app
    ADD CONSTRAINT uk5fe3e526a17b0e3825b9a5d09218 UNIQUE (app_id, user_id);


--
-- TOC entry 2301 (class 2606 OID 21144)
-- Name: app uk_88vfgccvckwwip06k7tpf8uk3; Type: CONSTRAINT; Schema: proxy; Owner: postgres
--

ALTER TABLE ONLY proxy.app
    ADD CONSTRAINT uk_88vfgccvckwwip06k7tpf8uk3 UNIQUE (name);


--
-- TOC entry 2306 (class 2606 OID 21146)
-- Name: config uk_kjjh66cda2b9nc24it8fhbfwx; Type: CONSTRAINT; Schema: proxy; Owner: postgres
--

ALTER TABLE ONLY proxy.config
    ADD CONSTRAINT uk_kjjh66cda2b9nc24it8fhbfwx UNIQUE (name);


--
-- TOC entry 2316 (class 2606 OID 21148)
-- Name: user uk_sb8bbouer5wak8vyiiy4pf2bx; Type: CONSTRAINT; Schema: proxy; Owner: postgres
--

ALTER TABLE ONLY proxy."user"
    ADD CONSTRAINT uk_sb8bbouer5wak8vyiiy4pf2bx UNIQUE (username);


--
-- TOC entry 2323 (class 2606 OID 21150)
-- Name: user_app user_app_pkey; Type: CONSTRAINT; Schema: proxy; Owner: postgres
--

ALTER TABLE ONLY proxy.user_app
    ADD CONSTRAINT user_app_pkey PRIMARY KEY (id);


--
-- TOC entry 2319 (class 2606 OID 21152)
-- Name: user user_pkey; Type: CONSTRAINT; Schema: proxy; Owner: postgres
--

ALTER TABLE ONLY proxy."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- TOC entry 2297 (class 1259 OID 21153)
-- Name: app_idx; Type: INDEX; Schema: proxy; Owner: postgres
--

CREATE INDEX app_idx ON proxy.app USING btree (name);


--
-- TOC entry 2302 (class 1259 OID 21154)
-- Name: config_idx; Type: INDEX; Schema: proxy; Owner: postgres
--

CREATE INDEX config_idx ON proxy.config USING btree (name);


--
-- TOC entry 2317 (class 1259 OID 21155)
-- Name: user_idx; Type: INDEX; Schema: proxy; Owner: postgres
--

CREATE INDEX user_idx ON proxy."user" USING btree (username);


--
-- TOC entry 2324 (class 1259 OID 21156)
-- Name: userapp_idx; Type: INDEX; Schema: proxy; Owner: postgres
--

CREATE INDEX userapp_idx ON proxy.user_app USING btree (app_id, user_id);


--
-- TOC entry 2327 (class 2606 OID 21157)
-- Name: user_app fk29p6bviul5jfpefxfndl8un44; Type: FK CONSTRAINT; Schema: proxy; Owner: postgres
--

ALTER TABLE ONLY proxy.user_app
    ADD CONSTRAINT fk29p6bviul5jfpefxfndl8un44 FOREIGN KEY (app_id) REFERENCES proxy.app(id);


--
-- TOC entry 2325 (class 2606 OID 21162)
-- Name: consumer fk3cfqs070yqmsn6shthj4byqsn; Type: FK CONSTRAINT; Schema: proxy; Owner: postgres
--

ALTER TABLE ONLY proxy.consumer
    ADD CONSTRAINT fk3cfqs070yqmsn6shthj4byqsn FOREIGN KEY (user_app_id) REFERENCES proxy.user_app(id);


--
-- TOC entry 2328 (class 2606 OID 21167)
-- Name: user_app fkf1jbdm607lwl0gyrx9os870lr; Type: FK CONSTRAINT; Schema: proxy; Owner: postgres
--

ALTER TABLE ONLY proxy.user_app
    ADD CONSTRAINT fkf1jbdm607lwl0gyrx9os870lr FOREIGN KEY (user_id) REFERENCES proxy."user"(id);


--
-- TOC entry 2326 (class 2606 OID 21172)
-- Name: consumer fktlh27gxxjxhwlbmsis6bp6gyd; Type: FK CONSTRAINT; Schema: proxy; Owner: postgres
--

ALTER TABLE ONLY proxy.consumer
    ADD CONSTRAINT fktlh27gxxjxhwlbmsis6bp6gyd FOREIGN KEY (consumer_app_id) REFERENCES proxy.app(id);


-- Completed on 2019-07-01 11:08:00 CEST

--
-- PostgreSQL database dump complete
--

