allOpen {
    annotation("org.springframework.data.mongodb.core.mapping.Document")
}

dependencies {
    implementation(project(":common"))
    compileOnly(project(":chat"))

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
}
