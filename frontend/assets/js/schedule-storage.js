const SCHEDULE_SLOTS_KEY = "medireservas_schedule_slots";

const SLOT_TEMPLATES = Object.freeze([
    {scheduleSlotId: 7, doctorId: 1, dayOffset: 1, startTime: "09:00:00", endTime: "09:30:00"},
    {scheduleSlotId: 8, doctorId: 1, dayOffset: 1, startTime: "10:00:00", endTime: "10:30:00"},
    {scheduleSlotId: 9, doctorId: 1, dayOffset: 2, startTime: "14:00:00", endTime: "14:30:00"},
    {scheduleSlotId: 10, doctorId: 2, dayOffset: 1, startTime: "10:00:00", endTime: "10:30:00"},
    {scheduleSlotId: 11, doctorId: 2, dayOffset: 2, startTime: "15:00:00", endTime: "15:30:00"},
    {scheduleSlotId: 12, doctorId: 2, dayOffset: 3, startTime: "16:00:00", endTime: "16:30:00"}
]);

function getDateWithOffset(dayOffset) {
    const date = new Date();
    date.setDate(date.getDate() + dayOffset);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
}

export function initializeBaseScheduleSlots() {
    if (localStorage.getItem(SCHEDULE_SLOTS_KEY)) return;

    const slots = SLOT_TEMPLATES.map(({dayOffset, ...slot}) => ({
        ...slot,
        slotDate: getDateWithOffset(dayOffset),
        slotStatus: "DISPONIBLE",
        appointmentId: null
    }));
    localStorage.setItem(SCHEDULE_SLOTS_KEY, JSON.stringify(slots));
}

export function getScheduleSlots() {
    try {
        return JSON.parse(localStorage.getItem(SCHEDULE_SLOTS_KEY)) ?? [];
    } catch {
        return [];
    }
}

export function getAvailableScheduleSlots(doctorId) {
    return getScheduleSlots().filter(
        (slot) => slot.doctorId === Number(doctorId) && slot.slotStatus === "DISPONIBLE"
    );
}

export function reserveScheduleSlot(scheduleSlotId, appointmentId) {
    const slots = getScheduleSlots();
    const slotIndex = slots.findIndex(
        (slot) => slot.scheduleSlotId === Number(scheduleSlotId) && slot.slotStatus === "DISPONIBLE"
    );
    if (slotIndex < 0) return null;

    slots[slotIndex] = {
        ...slots[slotIndex],
        slotStatus: "RESERVADO",
        appointmentId: Number(appointmentId)
    };
    localStorage.setItem(SCHEDULE_SLOTS_KEY, JSON.stringify(slots));
    return slots[slotIndex];
}
