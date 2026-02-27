dependencies {
    implementation(project(":common"))
    compileOnly(project(":chat"))
    compileOnly(project(":core:user-core"))
    compileOnly(project(":core:todo-core"))

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
}
