tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":common"))
    implementation(project(":support:monitoring"))
    implementation(project(":core:user-core"))
    implementation(project(":core:todo-core"))
    implementation(project(":storage:db-core"))

    testImplementation(project(":tests:api-docs"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
}
