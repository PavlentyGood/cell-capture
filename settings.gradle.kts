rootProject.name = "cell-capture"

include("kernel:domain")
include("kernel:common")

include("lobby:domain")
include("lobby:restapi")
include("lobby:app")

include("game:domain")
include("game:usecase")
include("game:rest:api")
include("game:rest:endpoint")
include("game:persistence")
include("game:app")

include("tests")
