INSERT INTO notifications (user_id, notification_template_id, notification_channel, notification_title, notification_message, notification_status, sent_at) VALUES
(1, 1, 'EMAIL', 'Bienvenida', 'Hola María, bienvenido a MediReservas. Tu cuenta ha sido creada.', 'Enviada', '2026-05-15 09:00:00'),
(2, 2, 'SMS', 'Cita confirmada', 'Tu cita con Dr. López ha sido confirmada para el 20 de mayo a las 10:30.', 'Enviada', '2026-05-16 14:00:00'),
(3, NULL, 'PUSH', 'Aviso del sistema', 'El sistema estará en mantenimiento el domingo de 2:00 a 4:00 AM.', 'Pendiente', NULL),
(4, 3, 'EMAIL', 'Cita cancelada', 'Tu cita del 18 de mayo ha sido cancelada. Contacta para reagendar.', 'Enviada', '2026-05-17 08:30:00'),
(5, 4, 'SMS', 'Recordatorio', 'Te recordamos tu cita mañana a las 15:00 con Dra. Martínez.', 'Pendiente', NULL);