package ru.practicum.gateway.booking.enums;


import ru.practicum.gateway.common.exceptions.StateException;

public enum BookingState {
	// Все
	ALL,
	// Текущие
	CURRENT,
	// Будущие
	FUTURE,
	// Завершенные
	PAST,
	// Отклоненные
	REJECTED,
	// Ожидающие подтверждения
	WAITING;


	public static BookingState from(String state) {
		try {
			return BookingState.valueOf(state);
		} catch (IllegalArgumentException e) {
			throw new StateException("Unknown state: " + state);
		}
	}
}
