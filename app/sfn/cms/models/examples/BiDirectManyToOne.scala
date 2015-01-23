package sfn.cms.models.examples


object BiDirectManyToOne {
  def main(args: Array[String]) {
    val tree = createNodeFor(1, None)
    println(s"tree: $tree")
  }


  val TreeSource =
    Map(1 -> List(2, 3),
      2 -> List(4, 5, 6),
      4 -> List(),
      5 -> List(7),
      7 -> List(),
      6 -> List(),
      3 -> List())


  case class Node[A](
                      val nodeId: Int,
                      val nodeContent: A,
                      val parent: Option[() => Node[A]],
                      val children: List[Node[A]])

  def createNodeFor(nodeId: Int, parentOption: Option[() => Node[String]]): Node[String] = {
    lazy val node: Node[String] = Node(nodeId, s"content ${nodeId}", parentOption, childrenOf(nodeId, node))
    node
  }

  def childrenOf(nodeId: Int, node: => Node[String]): List[Node[String]] =
    TreeSource(nodeId).map(createNodeFor(_, Some(() => node)))

  def childrenOf(node: => Node[String]): List[Node[String]] = Nil

}
