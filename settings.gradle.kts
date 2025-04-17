rootProject.name = "cell-capture"

include("kernel:domain")

include("lobby:domain")
include("lobby:usecase")
include("lobby:rest")
include("lobby:rest:api")
include("lobby:rest:endpoint")
include("lobby:persistence")
include("lobby:publishing")
include("lobby:app")

include("game:domain")
include("game:usecase")
include("game:rest")
include("game:persistence")
include("game:listening")
include("game:app")

include("tests")
