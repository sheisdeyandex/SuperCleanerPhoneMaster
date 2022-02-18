package com.supers.cleaner.phonemaster
import io.reactivex.rxjava3.subjects.BehaviorSubject
class avAdview<T>(defaultValue: T) {
    var value: T = defaultValue
        set(value) {
            field = value

            observable.onNext(value)
        }
    val observable = BehaviorSubject.createDefault(value)
}