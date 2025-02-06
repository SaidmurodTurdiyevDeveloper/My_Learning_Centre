package us.smt.mylearningcentre.domen.use_case.helper.student

import us.smt.mylearningcentre.domen.repository.StudentRepository

@JvmInline
value class GetUser(private val studentRepository: StudentRepository) {
    operator fun invoke() = studentRepository.getUser()
}