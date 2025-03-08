INSERT INTO crypto_user (username, created_at)
VALUES ('demoUser', CURRENT_TIMESTAMP);

INSERT INTO wallet (user_id, currency, balance, updated_at)
VALUES (1, 'USDT', 50000, CURRENT_TIMESTAMP);
