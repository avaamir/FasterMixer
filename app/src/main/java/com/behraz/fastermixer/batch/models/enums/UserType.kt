package com.behraz.fastermixer.batch.models.enums

enum class UserType(val roleId: Int) {
    Pomp(1),
    Mixer(2),
    Batch(3),
    Admin(4)
}