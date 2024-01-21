package navigator

import util.Navigator

class IosNavigator : Navigator {
    private var rootViewController: UIViewController? = null

    fun bind(rootViewController: UIViewController) {
        this.rootViewController = UIViewController()
    }

    override fun navigate(tag: String) {
        val secondViewController = SecondViewController()
        rootViewController?.presentViewController(
            secondViewController,
            animated = true,
            completion = null
        )
    }
}