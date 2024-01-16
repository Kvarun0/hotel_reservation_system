USE hotel_reservation_system;

CREATE TABLE reservationDB(
	reservation_id INT AUTO_INCREMENT PRIMARY KEY,
	guest_name VARCHAR(255) NOT NULL,
	room_number INT NOT NULL,
	contact_number VARCHAR(255) NOT NULL,
	reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	visibility BOOLEAN DEFAULT TRUE
);

-- for viewing all data
SELECT * FROM reservation;

--for viewwing none deleted data
SELECT * FROM reservationDB WHERE visibility=1;



-- insert data
INSERT INTO reservationDB(guest_name,room_number,contact_number) VALUES ("varun",1012,"984");

-- delete data
-- just delete
UPDATE reservationDB SET visibility=0 WHERE reservation_id=1;
-- permenent delete
DELETE * FROM reservationDB WHERE reservation_id = 0102;


-- update data 
UPDATE reservation SET guest_name="xyz" , room_number=3333 WHERE reservation_id=3030;
