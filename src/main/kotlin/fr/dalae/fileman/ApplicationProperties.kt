package fr.dalae.fileman

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated

/**
 * Properties are configured in the `application.yml` file.
 */
@Validated
@ConstructorBinding
@ConfigurationProperties("application")
data class ApplicationProperties(val storagePath: String, val batchSize: Int)
