package us.smt.mylearningcentre.domen.use_case.helper.student

import us.smt.mylearningcentre.domen.repository.StudentRepository

@JvmInline
value class GetAllClubStudent(private val studentRepository: StudentRepository) {
    operator fun invoke(clubId: String) = studentRepository.getAllClubStudent(clubId = clubId)
}