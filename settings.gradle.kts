rootProject.name = "cell-capture"

include("kernel:domain")
include("kernel:common")

include("lobby:domain")
include("lobby:restapi")
include("lobby:app")

include("game:domain")
include("game:restapi")
include("game:app")

include("tests")
