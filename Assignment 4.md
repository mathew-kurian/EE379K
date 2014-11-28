#**Assignment 4**
**Requirements Engineer (RE): ** Mathew Kurian  
**Date: ** 11/27/2014  

###4.1.1

The `University::StudentIdMustBeLength4` invariant fails because the `Student` object called `fred` has an attribute called `id` with a length greater than 4. In the specific case, “fred” has the id defined as `56789` which has a length of `5`. The invariant fails any object with an `id` of length `> 4` which marks `fred` as an invalid object.

###4.1.2
```
constraints

context University
    -- A students' id number must be unique
    inv StudentIdMustBeUnique:
       self.students->forAll(s, a | s <> a implies s.id <> a.id)
```

###4.1.3
```
checking invariant (2) `University::UniqueId': FAILED.
  -> false : Boolean
Results of subexpressions:
  University.allInstances : Set(University) = Set{@ut}
  self : University = @ut
  self.students : Set(Student) = Set{@fred,@sam,@sue}
  s : Student = @fred
  a : Student = @fred
  (s <> a) : Boolean = false
  ((s <> a) implies (s.id <> a.id)) : Boolean = true
  s : Student = @fred
  a : Student = @sam
  (s <> a) : Boolean = true
  s : Student = @fred
  s.id : String = '56789'
  a : Student = @sam
  a.id : String = '1234'
  (s.id <> a.id) : Boolean = true
  ((s <> a) implies (s.id <> a.id)) : Boolean = true
  s : Student = @fred
  a : Student = @sue
  (s <> a) : Boolean = true
  s : Student = @fred
  s.id : String = '56789'
  a : Student = @sue
  a.id : String = '1234'
  (s.id <> a.id) : Boolean = true
  ((s <> a) implies (s.id <> a.id)) : Boolean = true
  s : Student = @sam
  a : Student = @fred
  (s <> a) : Boolean = true
  s : Student = @sam
  s.id : String = '1234'
  a : Student = @fred
  a.id : String = '56789'
  (s.id <> a.id) : Boolean = true
  ((s <> a) implies (s.id <> a.id)) : Boolean = true
  s : Student = @sam
  a : Student = @sam
  (s <> a) : Boolean = false
  ((s <> a) implies (s.id <> a.id)) : Boolean = true
  s : Student = @sam
  a : Student = @sue
  (s <> a) : Boolean = true
  s : Student = @sam
  s.id : String = '1234'
  a : Student = @sue
  a.id : String = '1234'
  (s.id <> a.id) : Boolean = false
  ((s <> a) implies (s.id <> a.id)) : Boolean = false
  self.students->forAll(s, a : Student | ((s <> a) implies (s.id <> a.id))) : Boolean = false
  University.allInstances->forAll(self : University | self.students->forAll(s, a : Student | ((s <> a) implies (s.id <> a.id)))) : Boolean = false
checked 2 invariants in 0.019s, 2 failures.
```

###4.1.4
```
constraints

context University
    -- A student cannot be both grad and undergrad
    inv StudentIsGradOrUndergrad:
        self.graduates->forAll(s | self.undergraduates->forAll(r | s <> r))
```
###4.1.5
```
checking invariant (1) `University::StudentIsGradOrUndergrad': FAILED.
  -> false : Boolean
Results of subexpressions:
  University.allInstances : Set(University) = Set{@ut}
  self : University = @ut
  self.graduates : Set(Student) = Set{@sam}
  self : University = @ut
  self.undergraduates : Set(Student) = Set{@sam}
  s : Student = @sam
  r : Student = @sam
  (s <> r) : Boolean = false
  self.undergraduates->forAll(r : Student | (s <> r)) : Boolean = false
  self.graduates->forAll(s : Student | self.undergraduates->forAll(r : Student | (s <> r))) : Boolean = false
  University.allInstances->forAll(self : University | self.graduates->forAll(s : Student | self.undergraduates->forAll(r : Student | (s <> r)))) : Boolean = false
checked 1 invariant in 0.009s, 1 failure.
```
###4.1.6
```
constraints

context University
    --Ensures student do not exceed the approved maximum hours
    inv checkStudentHours:
        self.students->forAll(s | (s.takingCourses->size) * 3 <= s.maxApprovedSemesterHours)
```
###4.1.7
```
checking invariant (1) `University::checkStudentHours': FAILED.
  -> false : Boolean
Results of subexpressions:
  University.allInstances : Set(University) = Set{@ut}
  self : University = @ut
  self.students : Set(Student) = Set{@sam}
  s : Student = @sam
  s.takingCourses : Set(Course) = Set{@BUS311,@CS306,@E306,@EE302,@EE323,@EE338,@EE379K}
  s.takingCourses->size : Integer = 7
  3 : Integer = 3
  (s.takingCourses->size * 3) : Integer = 21
  s : Student = @sam
  s.maxApprovedSemesterHours : Integer = 18
  ((s.takingCourses->size * 3) <= s.maxApprovedSemesterHours) : Boolean = false
  self.students->forAll(s : Student | ((s.takingCourses->size * 3) <= s.maxApprovedSemesterHours)) : Boolean = false
  University.allInstances->forAll(self : University | self.students->forAll(s : Student | ((s.takingCourses->size * 3) <= s.maxApprovedSemesterHours))) : Boolean = false
checked 1 invariant in 0.004s, 1 failure.
```
4.2.1
```
model CourseRegistration_2

-- classes

class University
end

class Student
operations
  drop(c : Course)
end

class Course
attributes
  isFull : Boolean
end

-- associations

association EnrolledAtUniversity between
  Student[*] role students
  University[0..1] role isEnrolledAt
end

association TakingCourse between
  Student[*] role studentsEnrolled
  Course[*] role takingCourses
end

-- OCL constraints

constraints

context Student::drop(c : Course)
    -- Student registered for Course c
    pre RegisteredForCourse: takingCourses->includes(c)
    -- Student registerd for more than one course
    pre RegisteredForMultipleCourses: takingCourses->size > 1
    -- Student successfully dropped only Course c
    post CourseDropped: takingCourses->excludes(c)
    -- Course is no longer full
    post CourseNotFull: c.isFull = false
    -- Student enrolled still enrolled at University
    -- Question: Do we need to check which universiy?
    post StudentEnrolled: isEnrolledAt->size = 1
    -- Only one student student removed from Course
    post onlyThisStudent: c.studentsEnrolled@pre->size = c.studentsEnrolled->size + 1
```
###4.2.2
```
-- only one university defined
!create ut : University

-- at least 2 students
!create sam : Student
!create sue : Student

-- at least 7 courses defined
!create EE302 : Course
!create CS306 : Course
!create BUS311 : Course
!create EE411 : Course
!create EE379K : Course
!create E306 : Course
!create EE338 : Course
!create EE323 : Course

-- both students enrolled at university
!insert (sam,ut) into EnrolledAtUniversity
!insert (sue,ut) into EnrolledAtUniversity

-- both students registered for courses intend to drop
!insert (sam,CS306) into TakingCourse
!insert (sue,CS306) into TakingCourse

-- course to be dropped is full
!set CS306.isFull := true

-- both students registered for other courses
!insert (sam,E306) into TakingCourse
!insert (sam,BUS311) into TakingCourse
!insert (sam,EE379K) into TakingCourse
!insert (sam,EE323) into TakingCourse
!insert (sam,EE411) into TakingCourse
!insert (sam,EE338) into TakingCourse

!insert (sue,E306) into TakingCourse
!insert (sue,BUS311) into TakingCourse
!insert (sue,EE379K) into TakingCourse
!insert (sue,EE323) into TakingCourse
!insert (sue,EE411) into TakingCourse
!insert (sue,EE338) into TakingCourse

-- peform drop
!openter sam drop(CS306)
    !delete (sam,CS306) from TakingCourse
    !set CS306.isFull := false
!opexit
```
###4.2.3
```
CR2.cmd> -- only one university defined
CR2.cmd> !create ut : University
CR2.cmd>
CR2.cmd> -- at least 2 students
CR2.cmd> !create sam : Student
CR2.cmd> !create sue : Student
CR2.cmd>
CR2.cmd> -- at least 7 courses defined
CR2.cmd> !create EE302 : Course
CR2.cmd> !create CS306 : Course
CR2.cmd> !create BUS311 : Course
CR2.cmd> !create EE411 : Course
CR2.cmd> !create EE379K : Course
CR2.cmd> !create E306 : Course
CR2.cmd> !create EE338 : Course
CR2.cmd> !create EE323 : Course
CR2.cmd>
CR2.cmd> -- both students enrolled at university
CR2.cmd> !insert (sam,ut) into EnrolledAtUniversity
CR2.cmd> !insert (sue,ut) into EnrolledAtUniversity
CR2.cmd>
CR2.cmd> -- both students registered for courses intend to drop
CR2.cmd> !insert (sam,CS306) into TakingCourse
CR2.cmd> !insert (sue,CS306) into TakingCourse
CR2.cmd>
CR2.cmd> -- course to be dropped is full
CR2.cmd> !set CS306.isFull := true
CR2.cmd>
CR2.cmd> -- both students registered for other courses
CR2.cmd> !insert (sam,E306) into TakingCourse
CR2.cmd> !insert (sam,BUS311) into TakingCourse
CR2.cmd> !insert (sam,EE379K) into TakingCourse
CR2.cmd> !insert (sam,EE323) into TakingCourse
CR2.cmd> !insert (sam,EE411) into TakingCourse
CR2.cmd> !insert (sam,EE338) into TakingCourse
CR2.cmd>
CR2.cmd> !insert (sue,E306) into TakingCourse
CR2.cmd> !insert (sue,BUS311) into TakingCourse
CR2.cmd> !insert (sue,EE379K) into TakingCourse
CR2.cmd> !insert (sue,EE323) into TakingCourse
CR2.cmd> !insert (sue,EE411) into TakingCourse
CR2.cmd> !insert (sue,EE338) into TakingCourse
CR2.cmd>
CR2.cmd> -- peform drop
CR2.cmd> !openter sam drop(CS306)
precondition `RegisteredForCourse' is true
precondition `RegisteredForMultipleCourses' is true
CR2.cmd>     !delete (sam,CS306) from TakingCourse
CR2.cmd>     !set CS306.isFull := false
CR2.cmd> !opexit
postcondition `CourseDropped' is true
postcondition `CourseNotFull' is true
postcondition `StudentEnrolled' is true
postcondition `OnlyThisStudent' is true
```
