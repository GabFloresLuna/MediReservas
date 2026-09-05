function createSummary(value, label) {
    return {value: String(value), label};
}

export function getDashboardSummary({session, users = [], specialties = [], appointments = [], today}) {
    if (!session) return [];

    if (session.role === "ADMINISTRADOR") {
        return [
            createSummary(new Set(users.map((user) => user.role)).size, "Perfiles del sistema"),
            createSummary(users.filter((user) => user.active).length, "Usuarios activos"),
            createSummary(specialties.filter((specialty) => specialty.active).length, "Especialidades activas")
        ];
    }

    if (session.role === "RECEPCIONISTA") {
        return [
            createSummary(appointments.filter((appointment) => appointment.status === "PENDIENTE").length, "Citas pendientes"),
            createSummary(appointments.filter((appointment) => appointment.status === "CONFIRMADA").length, "Citas confirmadas"),
            createSummary(appointments.filter((appointment) => appointment.status === "REAGENDADA").length, "Citas reagendadas")
        ];
    }

    if (session.role === "MEDICO") {
        const doctorAppointments = appointments.filter((appointment) => appointment.doctorId === session.userId);
        return [
            createSummary(doctorAppointments.filter((appointment) => appointment.date === today && appointment.status !== "CANCELADA").length, "Atenciones de hoy"),
            createSummary(doctorAppointments.filter((appointment) => appointment.status === "CONFIRMADA" && appointment.date <= today).length, "Observaciones pendientes"),
            createSummary(doctorAppointments.filter((appointment) => appointment.status === "COMPLETADA").length, "Atenciones completadas")
        ];
    }

    if (session.role === "PACIENTE") {
        const patientRun = users.find((user) => user.id === session.userId)?.run ?? session.run;
        const patientAppointments = appointments.filter(
            (appointment) => appointment.patientId === session.userId || appointment.patientRun === patientRun
        );
        return [
            createSummary(
                patientAppointments.filter(
                    (appointment) => appointment.date >= today && !["CANCELADA", "COMPLETADA"].includes(appointment.status)
                ).length,
                "Próximas citas"
            ),
            createSummary(patientAppointments.filter((appointment) => appointment.status === "PENDIENTE").length, "Solicitudes pendientes"),
            createSummary(patientAppointments.filter((appointment) => appointment.status === "COMPLETADA").length, "Atenciones realizadas")
        ];
    }

    return [];
}
