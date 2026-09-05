const STATUS_LABELS = Object.freeze({
    PENDIENTE: "Pendiente",
    CONFIRMADA: "Confirmada",
    REAGENDADA: "Reagendada",
    CANCELADA: "Cancelada",
    COMPLETADA: "Completada"
});

const STATUS_BADGE_CLASSES = Object.freeze({
    PENDIENTE: "inline-flex rounded-full bg-amber-50 px-3 py-1 text-xs font-bold text-amber-700",
    CONFIRMADA: "inline-flex rounded-full bg-emerald-50 px-3 py-1 text-xs font-bold text-primary-dark",
    REAGENDADA: "inline-flex rounded-full bg-blue-50 px-3 py-1 text-xs font-bold text-secondary",
    CANCELADA: "inline-flex rounded-full bg-red-50 px-3 py-1 text-xs font-bold text-red-700",
    COMPLETADA: "inline-flex rounded-full bg-blue-50 px-3 py-1 text-xs font-bold text-blue-700"
});

export function getAppointmentStatusLabel(status) {
    return STATUS_LABELS[status] ?? status;
}

export function getAppointmentStatusBadgeClass(status) {
    return STATUS_BADGE_CLASSES[status] ?? STATUS_BADGE_CLASSES.PENDIENTE;
}

export function canCancelAppointment(status) {
    return ["PENDIENTE", "CONFIRMADA", "REAGENDADA"].includes(status);
}

export function formatAppointmentDate(date) {
    return new Intl.DateTimeFormat("es-CL", {timeZone: "UTC"}).format(new Date(`${date}T00:00:00Z`));
}
