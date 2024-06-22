package org.springframework.samples.petclinic.model


import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import java.util.*
import jakarta.validation.Validator

/**
 * @author Michael Isvy
 * Simple test to make sure that Bean Validation is working
 * (useful when upgrading to a new version of Hibernate Validator/ Bean Validation)
 */
class ValidatorTests {

    // create a validator by LocalValidatorFactoryBean
    // 왜 Local 인가?
    // spring framework 의 applicationContext 에서 관리되고, 어플리케이션의 생명주기에 따라 소멸되기 때문이다.
    private fun createValidator(): Validator {
        val localValidatorFactoryBean = LocalValidatorFactoryBean()
        localValidatorFactoryBean.afterPropertiesSet()
        return localValidatorFactoryBean
    }

    @Test
    fun shouldNotValidateWhenFirstNameEmpty() {

        // spring framework will use the default locale.
        // validation logs will be in English
        LocaleContextHolder.setLocale(Locale.ENGLISH)
        val person = Person()
        person.firstName = ""
        person.lastName = "smith"

        // get
        val validator = createValidator()
        // save validate result by Set type
        // propertyPath : 오류가 발생산 속셩의 경로
        // message : 오류 메시지
        // invalidValue : 오류가 발생한 값
        // rootBean : 유효성 검사가 수행된 루트 객체
        // leafBean : 유효성 검사가 수행된 최하위 객체

        val constraintViolations = validator.validate(person)

        assertThat(constraintViolations).hasSize(1)
        val violation = constraintViolations.iterator().next()
        assertThat(violation.propertyPath.toString()).isEqualTo("firstName")
        assertThat(violation.message).isEqualTo("must not be empty")
        assertThat(violation.invalidValue).isEqualTo("")
        assertThat(violation.rootBean).isEqualTo(person)
        assertThat(violation.leafBean).isEqualTo(person)
    }

}
