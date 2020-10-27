INSERT INTO USER
VALUES (1, true, 'admin@localhost', 'admin', 'Admin Istrator',
        '$6$rounds=10000$fpZlqmJj7S7OO6jM$UgLmxptIv4j63LFZlCs0TAMhhTBmsi/EUkeMTnYgs.ROumw8iblXQzs.mUsNj4DIm5oCT10RbzOmmajKM1seg.',
        false);

INSERT INTO KPI
VALUES (1, 'cpu', 'PERCENT');

INSERT INTO KPI
VALUES (2, 'disk_usage', 'KBIT');

INSERT INTO KPI
VALUES (3, 'memory', 'MB');

INSERT INTO KPI
VALUES (4, 'net_bytes_send', 'KBIT');

INSERT INTO KPI
VALUES (5, 'net_bytes_recv', 'KBIT');

INSERT INTO KPI
VALUES (6, 'power_current', 'AMPERE');

INSERT INTO KPI
VALUES (7, 'power_voltage', 'VOLT');

INSERT INTO KPI
VALUES (8, 'power_usage', 'WATT');

