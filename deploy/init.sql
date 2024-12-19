CREATE DATABASE "configurations";
CREATE DATABASE "orders";
CREATE DATABASE "business-orders";
CREATE DATABASE "services";
CREATE DATABASE "statistics";
CREATE DATABASE "notifications";
CREATE DATABASE "chatdb";

GRANT ALL PRIVILEGES ON DATABASE "configurations" TO postgres;
GRANT ALL PRIVILEGES ON DATABASE "orders" TO postgres;
GRANT ALL PRIVILEGES ON DATABASE "chatdb" TO postgres;
GRANT ALL PRIVILEGES ON DATABASE "business-orders" TO postgres;
GRANT ALL PRIVILEGES ON DATABASE "services" TO postgres;
GRANT ALL PRIVILEGES ON DATABASE "statistics" TO postgres;
GRANT ALL PRIVILEGES ON DATABASE "notifications" TO postgres;