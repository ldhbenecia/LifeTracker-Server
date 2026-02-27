tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":common"))
    implementation(project(":support:monitoring"))
    implementation(project(":support:logging"))
    implementation(project(":core:user-core"))
    implementation(project(":core:todo-core"))
    implementation(project(":core:ledger-core"))
    implementation(project(":storage:db-core-jpa"))
    implementation(project(":storage:db-core-mongo"))
    implementation(project(":storage:db-core-redis"))
    implementation(project(":chat"))

    testImplementation(project(":tests:api-docs"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
}
