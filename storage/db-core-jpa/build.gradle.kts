apply(plugin = "org.jetbrains.kotlin.plugin.jpa")

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

dependencies {
    implementation(project(":common"))
    compileOnly(project(":chat"))
    compileOnly(project(":core:user-core"))
    compileOnly(project(":core:todo-core"))
    compileOnly(project(":core:ledger-core"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.mysql:mysql-connector-j")
}
