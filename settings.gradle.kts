rootProject.name = "cell-capture"

include("kernel:domain")
include("kernel:common")

include("lobby:domain")
include("lobby:usecase")
include("lobby:rest")
include("lobby:rest:api")
include("lobby:rest:endpoint")
include("lobby:persistence")
include("lobby:app")

include("game:domain")
include("game:usecase")
include("game:rest:api")
include("game:rest:endpoint")
include("game:persistence")
include("game:app")

include("tests")
